package com.example.gbscollectionform;

import static com.example.gbscollectionform.DBHandler.COL_BARANGAY;
import static com.example.gbscollectionform.DBHandler.COL_COLLECTOR;
import static com.example.gbscollectionform.DBHandler.COL_MUNICIPALITY;
import static com.example.gbscollectionform.DBHandler.COL_PROVINCE;
import static com.example.gbscollectionform.DBHandler.COL_QUANTITY;
import static com.example.gbscollectionform.DBHandler.COL_REGION;
import static com.example.gbscollectionform.DBHandler.COL_REPRESENTATIVE;
import static com.example.gbscollectionform.DBHandler.COL_STREET;
import static com.example.gbscollectionform.DBHandler.COL_TOTAL_PRICE;
import static com.example.gbscollectionform.DBHandler.TABLE_COLLECTIONS;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Edit extends AppCompatActivity {

    Button update_btn;
    EditText region, province, municipality, barangay, street, weight, agreed_price, vendor, collector;

    SharedPreferences get;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        region = findViewById(R.id.region);
        province = findViewById(R.id.province);
        municipality = findViewById(R.id.municipality);
        barangay = findViewById(R.id.barangay);

        street = findViewById(R.id.street);
        weight = findViewById(R.id.weight);
        agreed_price = findViewById(R.id.agreed_price);
        vendor = findViewById(R.id.vendor);
        collector = findViewById(R.id.collector);

        get = getSharedPreferences("this_preferences", MODE_PRIVATE);
        int _id = get.getInt("_id", 0);
        String iid = String.valueOf(_id);

        DBHandler db = new DBHandler(Edit.this);

        try (SQLiteDatabase dbRead = db.getReadableDatabase();
             Cursor cursor = dbRead.rawQuery("SELECT * FROM " + TABLE_COLLECTIONS + " WHERE id = ?", new String[]{iid})) {

            if (cursor.moveToFirst()) {
                region.setText(cursor.getString(cursor.getColumnIndex(COL_REGION)));
                province.setText(cursor.getString(cursor.getColumnIndex(COL_PROVINCE)));
                municipality.setText(cursor.getString(cursor.getColumnIndex(COL_MUNICIPALITY)));
                barangay.setText(cursor.getString(cursor.getColumnIndex(COL_BARANGAY)));
                street.setText(cursor.getString(cursor.getColumnIndex(COL_STREET)));
                weight.setText(cursor.getString(cursor.getColumnIndex(COL_QUANTITY)));


                double t_price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COL_TOTAL_PRICE)));
                double quant = Double.parseDouble(cursor.getString(cursor.getColumnIndex(COL_QUANTITY)));
                String agreed = String.valueOf(t_price/quant);


                agreed_price.setText(agreed);
                vendor.setText(cursor.getString(cursor.getColumnIndex(COL_REPRESENTATIVE)));
                collector.setText(cursor.getString(cursor.getColumnIndex(COL_COLLECTOR)));

            }
        }

        update_btn = findViewById(R.id.update_btn);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Edit.this)
                        .setTitle("Confirm Update")
                        .setMessage("Are you sure you want to update this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues values = new ContentValues();
                                values.put(COL_REGION, region.getText().toString());
                                values.put(COL_PROVINCE, province.getText().toString());
                                values.put(COL_MUNICIPALITY, municipality.getText().toString());
                                values.put(COL_BARANGAY, barangay.getText().toString());
                                values.put(COL_STREET, street.getText().toString());
                                values.put(COL_QUANTITY, weight.getText().toString());
                                values.put(COL_TOTAL_PRICE, Double.parseDouble(weight.getText().toString()) * Double.parseDouble(agreed_price.getText().toString()));
                                values.put(COL_REPRESENTATIVE, vendor.getText().toString());
                                values.put(COL_COLLECTOR, collector.getText().toString());

                                SQLiteDatabase dbWrite = db.getWritableDatabase();
                                int rowsAffected = dbWrite.update(TABLE_COLLECTIONS, values, "id = ?", new String[]{iid});

                                if (rowsAffected > 0) {
                                    Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Close the dialog
                            }
                        })
                        .create()
                        .show();
            }
        });


    }
}
