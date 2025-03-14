package com.example.gbscollectionform;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.example.gbscollectionform.DBHandler.COL_BARANGAY;
import static com.example.gbscollectionform.DBHandler.COL_CLASSIFICATION;
import static com.example.gbscollectionform.DBHandler.COL_COLLECTION_DATE;
import static com.example.gbscollectionform.DBHandler.COL_COLLECTOR;
import static com.example.gbscollectionform.DBHandler.COL_COLLECTOR_ID;
import static com.example.gbscollectionform.DBHandler.COL_COLOR;
import static com.example.gbscollectionform.DBHandler.COL_EID;
import static com.example.gbscollectionform.DBHandler.COL_ESTABLISHMENT_ID;
import static com.example.gbscollectionform.DBHandler.COL_EST_BARANGAY;
import static com.example.gbscollectionform.DBHandler.COL_EST_DATE_AND_TIME;
import static com.example.gbscollectionform.DBHandler.COL_EST_IMAGE_PATH;
import static com.example.gbscollectionform.DBHandler.COL_EST_IN_CHARGE;
import static com.example.gbscollectionform.DBHandler.COL_EST_MUNICIPALITY;
import static com.example.gbscollectionform.DBHandler.COL_EST_NAME;
import static com.example.gbscollectionform.DBHandler.COL_EST_PROVINCE;
import static com.example.gbscollectionform.DBHandler.COL_EST_REGION;
import static com.example.gbscollectionform.DBHandler.COL_EST_STREET;
import static com.example.gbscollectionform.DBHandler.COL_EST_TYPE;
import static com.example.gbscollectionform.DBHandler.COL_ID;
import static com.example.gbscollectionform.DBHandler.COL_IMAGE_PATH;
import static com.example.gbscollectionform.DBHandler.COL_JID;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_BARANGAY;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_DATE_AND_TIME;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_ID;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_IMAGE_PATH;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_IN_CHARGE;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_MUNICIPALITY;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_NAME;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_PROVINCE;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_REGION;
import static com.example.gbscollectionform.DBHandler.COL_JUNKSHOP_STREET;
import static com.example.gbscollectionform.DBHandler.COL_MUNICIPALITY;
import static com.example.gbscollectionform.DBHandler.COL_PROVINCE;
import static com.example.gbscollectionform.DBHandler.COL_QUANTITY;
import static com.example.gbscollectionform.DBHandler.COL_REGION;
import static com.example.gbscollectionform.DBHandler.COL_REPRESENTATIVE;
import static com.example.gbscollectionform.DBHandler.COL_STREET;
import static com.example.gbscollectionform.DBHandler.COL_TOTAL_PRICE;
import static com.example.gbscollectionform.DBHandler.TABLE_COLLECTIONS;
import static com.example.gbscollectionform.DBHandler.TABLE_ESTABLISHMENT;
import static com.example.gbscollectionform.DBHandler.TABLE_JUNKSHOP;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewOfflineRecord extends AppCompatActivity {

    private DBHandler dbHandler;

    String domain = "192.168.100.224";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offline_record);

        dbHandler = new DBHandler(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Record> records = read();
        RecordAdapter recordAdapter = new RecordAdapter(records);
        recyclerView.setAdapter(recordAdapter);

        ImageView back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_home = new Intent(NewOfflineRecord.this, Form.class);
                startActivity(back_home);
            }
        });

        Button save_online = findViewById(R.id.save_online);
        save_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Save Online")
                        .setMessage("Are you sure you want to save the record online?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sendAllJunkshopData();
                                sendAllEstData();
                                sendAllCollectionData();

                                DBHandler dbHelper = new DBHandler(NewOfflineRecord.this);
                                dbHelper.delete_AllC_record(NewOfflineRecord.this
                                );
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }
    private void sendAllJunkshopData() {
        String url = "https://" + domain + "/Collection/api/sync_junkshop.php";
        RequestQueue queue = Volley.newRequestQueue(this, new CustomHurlStack());

        // Fetch all data from SQLite
        DBHandler dbHandler = new DBHandler(this);
        List<Map<String, String>> dataList = dbHandler.getAllJunkshopData();

        // Build POST parameters as a single String
        StringBuilder postDataBuilder = new StringBuilder();
        int rowCount = 0;

        for (Map<String, String> row : dataList) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                try {
                    // Ensure the key and value are not null before encoding
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (key != null && value != null) {
                        // Properly encode each key and value
                        String encodedKey = "row" + rowCount + "_" + URLEncoder.encode(key, "UTF-8");
                        String encodedValue = URLEncoder.encode(value, "UTF-8");
                        postDataBuilder.append(encodedKey).append("=").append(encodedValue).append("&");
                    } else {
                        Log.e("NullValue", "Null key or value encountered. Skipping...");
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.e("EncodingError", "Error encoding parameter", e);
                }
            }
            rowCount++;
        }

        String postData = postDataBuilder.toString();

        // Remove the trailing '&'
        if (postData.endsWith("&")) {
            postData = postData.substring(0, postData.length() - 1);
        }

        final String finalPostData = postData;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("ServerResponse", response); // Log the server response
        }, error -> {
            Log.e("VolleyError", error.toString()); // Log errors
        }) {
            @Override
            public byte[] getBody() {
                return finalPostData.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        // Add the request to the queue
        queue.add(stringRequest);
    }

    private void sendAllEstData() {
        String url = "https://" + domain + "Collection/api/sync_establishment.php";
        RequestQueue queue = Volley.newRequestQueue(this, new CustomHurlStack());

        // Fetch all data from SQLite
        DBHandler dbHandler = new DBHandler(this);
        List<Map<String, String>> dataList = dbHandler.getAllEstData();

        // Build POST parameters as a single String
        StringBuilder postDataBuilder = new StringBuilder();
        int rowCount = 0;

        for (Map<String, String> row : dataList) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                try {
                    // Ensure the key and value are not null before encoding
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (key != null && value != null) {
                        // Properly encode each key and value
                        String encodedKey = "row" + rowCount + "_" + URLEncoder.encode(key, "UTF-8");
                        String encodedValue = URLEncoder.encode(value, "UTF-8");
                        postDataBuilder.append(encodedKey).append("=").append(encodedValue).append("&");
                    } else {
                        Log.e("NullValue", "Null key or value encountered. Skipping...");
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.e("EncodingError", "Error encoding parameter", e);
                }
            }
            rowCount++;
        }

        String postData = postDataBuilder.toString();

        // Remove the trailing '&'
        if (postData.endsWith("&")) {
            postData = postData.substring(0, postData.length() - 1);
        }

        final String finalPostData = postData;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("ServerResponse", response); // Log the server response
        }, error -> {
            Log.e("VolleyError", error.toString()); // Log errors
        }) {
            @Override
            public byte[] getBody() {
                return finalPostData.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        // Add the request to the queue
        queue.add(stringRequest);
    }

    private void sendAllCollectionData() {
        String url = "https://"  + domain + "/Collection/api/sync_collection.php";
        RequestQueue queue = Volley.newRequestQueue(this, new CustomHurlStack());

        // Fetch all data from SQLite
        DBHandler dbHandler = new DBHandler(this);
        List<Map<String, String>> dataList = dbHandler.getAllCollectionData();

        // Build POST parameters as a single String
        StringBuilder postDataBuilder = new StringBuilder();
        int rowCount = 0;

        for (Map<String, String> row : dataList) {
            for (Map.Entry<String, String> entry : row.entrySet()) {
                try {
                    // Ensure the key and value are not null before encoding
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (key != null && value != null) {
                        // Properly encode each key and value
                        String encodedKey = "row" + rowCount + "_" + URLEncoder.encode(key, "UTF-8");
                        String encodedValue = URLEncoder.encode(value, "UTF-8");
                        postDataBuilder.append(encodedKey).append("=").append(encodedValue).append("&");
                    } else {
                        Log.e("NullValue", "Null key or value encountered. Skipping...");
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.e("EncodingError", "Error encoding parameter", e);
                }
            }
            rowCount++;
        }

        String postData = postDataBuilder.toString();

        // Remove the trailing '&'
        if (postData.endsWith("&")) {
            postData = postData.substring(0, postData.length() - 1);
        }

        final String finalPostData = postData;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("ServerResponse", response); // Log the server response
        }, error -> {
            Log.e("VolleyError", error.toString()); // Log errors
        }) {
            @Override
            public byte[] getBody() {
                return finalPostData.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        // Add the request to the queue
        queue.add(stringRequest);
    }

    @SuppressLint("Range")
    public List<Record> read() {
        List<Record> recordsList = new ArrayList<>();

        try (SQLiteDatabase dbRead = dbHandler.getWritableDatabase();
             Cursor cursorCourses = dbRead.rawQuery("SELECT * FROM " + TABLE_JUNKSHOP, null)) {

            if (cursorCourses.moveToFirst()) {
                do {

                    Record record = new Record();

                    //JUNKSHOP_TABLE
                    record.j_id = Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_JID)));
                    record.j_date = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_DATE_AND_TIME));
                    record.j_name = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_NAME));
                    record.j_vendor = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_IN_CHARGE));
                    record.j_street = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_STREET));
                    record.j_brgy = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_BARANGAY));
                    record.j_lgu = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_MUNICIPALITY));
                    record.j_province = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_PROVINCE));
                    record.j_region = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_REGION));
                    record.j_image = cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_IMAGE_PATH));

                    recordsList.add(record);

                } while (cursorCourses.moveToNext());
            }
        }

        try (SQLiteDatabase dbRead = dbHandler.getWritableDatabase();
             Cursor cursorCourses = dbRead.rawQuery("SELECT * FROM " + TABLE_COLLECTIONS , null)) {

            if (cursorCourses.moveToFirst()) {
                do {

                    Record record = new Record();

                    //COLLECTION_TABLE
                    record._id = Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_ID)));
                    record._imagePath = cursorCourses.getString(cursorCourses.getColumnIndex(COL_IMAGE_PATH));
                    record._collectorId = Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_COLLECTOR_ID)));
                    record._estId =  Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_ESTABLISHMENT_ID)));
                    record._junkshopId =  Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_JUNKSHOP_ID)));
                    record._street = cursorCourses.getString(cursorCourses.getColumnIndex(COL_STREET));
                    record._barangay = cursorCourses.getString(cursorCourses.getColumnIndex(COL_BARANGAY));
                    record._municipality = cursorCourses.getString(cursorCourses.getColumnIndex(COL_MUNICIPALITY));
                    record._classification =  Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_CLASSIFICATION)));
                    record._province = cursorCourses.getString(cursorCourses.getColumnIndex(COL_PROVINCE));
                    record._region = cursorCourses.getString(cursorCourses.getColumnIndex(COL_REGION));
                    record._quantity = Double.parseDouble(cursorCourses.getString(cursorCourses.getColumnIndex(COL_QUANTITY)));
                    record._totalPrice = Double.parseDouble(cursorCourses.getString(cursorCourses.getColumnIndex(COL_TOTAL_PRICE)));
                    record._color =  Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_COLOR)));
                    record._vendor = cursorCourses.getString(cursorCourses.getColumnIndex(COL_REPRESENTATIVE));
                    record._collector = cursorCourses.getString(cursorCourses.getColumnIndex(COL_COLLECTOR));
                    record._date = cursorCourses.getString(cursorCourses.getColumnIndex(COL_COLLECTION_DATE));

                    recordsList.add(record);

                } while (cursorCourses.moveToNext());
            }
        }

        try (SQLiteDatabase dbRead = dbHandler.getWritableDatabase();
             Cursor cursorCourses = dbRead.rawQuery("SELECT * FROM " + TABLE_ESTABLISHMENT, null)) {

            if (cursorCourses.moveToFirst()) {
                do {

                    Record record = new Record();

                    //ESTABLISHMENT TABLE
                    record.e_id =  Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_EID)));
                    record.e_date = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_DATE_AND_TIME));
                    record.e_name = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_NAME));
                    record.e_vendor = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_IN_CHARGE));
                    record.e_street = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_STREET));
                    record.e_brgy = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_BARANGAY));
                    record.e_lgu = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_MUNICIPALITY));
                    record.e_province = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_PROVINCE));
                    record.e_region = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_REGION));
                    record.e_type =  Integer.parseInt(cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_TYPE)));
                    record.e_image = cursorCourses.getString(cursorCourses.getColumnIndex(COL_EST_IMAGE_PATH));

                    recordsList.add(record);

                } while (cursorCourses.moveToNext());
            }
        }

        return recordsList;
    }
}