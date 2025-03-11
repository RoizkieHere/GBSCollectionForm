package com.example.gbscollectionform;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.util.OptionalInt.empty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private final List<com.example.gbscollectionform.Record> records;

    public RecordAdapter(List<com.example.gbscollectionform.Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Record record = records.get(position);

        // Construct location string
        String s_location = "";
        if (record._street != null && !record._street.isEmpty()) {
            s_location = record._street + " ," + record._barangay + " ," + record._municipality + " ," + record._province;
        } else {
            s_location = record._barangay + " ," + record._municipality + " ," + record._province;
        }

        // Check if critical data is null or empty; if so, hide the row
        if (record._date == null || record._date.isEmpty() || s_location.isEmpty()) {
            holder.itemView.setVisibility(GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }

        // Set the visible data if all conditions are met
        holder._date.setText(String.valueOf(record._date));
        holder.location.setText(s_location);

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context con = v.getContext();

                // Create and show the confirmation dialog
                new AlertDialog.Builder(con)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHandler dbHelper = new DBHandler(con);
                                dbHelper.delete_c_record(record._id, con);

                                // Optional: Show a success message
                                Toast.makeText(con, "Record deleted successfully!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Close the dialog if the user selects "No"
                            }
                        })
                        .create()
                        .show();
            }
        });


        // Set up Edit button functionality
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                SharedPreferences sharedPreferences = context.getSharedPreferences("this_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("j_id", record.j_id);
                editor.putInt("e_id", record.e_id);
                editor.putInt("_id", record._id);
                editor.apply();

                Intent go_edit = new Intent(context, Edit.class);
                context.startActivity(go_edit);
            }
        });
    }


    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  _vendor,_street, _brgy, _lgu, _region, _date, location, no_record ;
        public ImageButton del, edit;
        public TableLayout table_data;


        public ViewHolder(View itemView) {
            super(itemView);

            _date = itemView.findViewById(R.id._date);
            location = itemView.findViewById(R.id.location);
            del = itemView.findViewById(R.id.del);
            edit = itemView.findViewById(R.id.edit);
            table_data = itemView.findViewById(R.id.table_data);

        }
    }
}
