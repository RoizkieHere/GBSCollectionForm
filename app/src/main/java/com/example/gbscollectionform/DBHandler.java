package com.example.gbscollectionform;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler extends SQLiteOpenHelper {

    private Context context;

    String colid;

    public int col_id;

    // Table and column constants
    public static final String TABLE_COLLECTIONS = "collections";
    public static final String TABLE_JUNKSHOP = "junkshop";
    public static final String TABLE_ESTABLISHMENT = "establishment";
    public static final String TABLE_COLLECTOR_ACCOUNT = "collector_account";

    //Collector Account Table Columns
    public static final String COLA_ID = "id";
    public static final String COLA_NAME = "name";
    public static final String COLA_ADDRESS = "address";
    public static final String COLA_PNUMBER = "phone_number";
    public static final String COLA_USERNAME = "username";
    public static final String COLA_PASSWORD = "password";
    public static final String COLA_DATE = "date";

    // Collections Table Columns
    public static final String COL_ID = "id";
    public static final String COL_IMAGE_PATH = "image_path";
    public static final String COL_COLLECTOR_ID = "collector_id";
    public static final String COL_ESTABLISHMENT_ID = "establishment_id";
    public static final String COL_JUNKSHOP_ID = "junkshop_id";
    public static final String COL_STREET = "street";
    public static final String COL_BARANGAY = "barangay";
    public static final String COL_MUNICIPALITY = "municipality";
    public static final String COL_CLASSIFICATION = "classification";
    public static final String COL_PROVINCE = "province";
    public static final String COL_REGION = "region";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_TOTAL_PRICE = "total_price";
    public static final String COL_COLOR = "color";
    public static final String COL_REPRESENTATIVE = "representative";
    public static final String COL_COLLECTOR = "collector";
    public static final String COL_COLLECTION_DATE = "collection_date";

    // Junkshop Table Columns
    public static final String COL_JID = "id";
    public static final String COL_JUNKSHOP_NAME = "name";
    public static final String COL_JUNKSHOP_IMAGE_PATH = "image_path";
    public static final String COL_JUNKSHOP_IN_CHARGE = "in_charge";
    public static final String COL_JUNKSHOP_STREET = "street";
    public static final String COL_JUNKSHOP_BARANGAY = "barangay";
    public static final String COL_JUNKSHOP_MUNICIPALITY = "municipality";
    public static final String COL_JUNKSHOP_PROVINCE = "province";
    public static final String COL_JUNKSHOP_REGION = "region";
    public static final String COL_JUNKSHOP_DATE_AND_TIME = "date_and_time";

    // Establishment Table Columns
    public static final String COL_EID = "id";
    public static final String COL_EST_NAME = "name";
    public static final String COL_EST_IMAGE_PATH = "image_path";
    public static final String COL_EST_IN_CHARGE = "in_charge";
    public static final String COL_EST_TYPE = "type";
    public static final String COL_EST_STREET = "street";
    public static final String COL_EST_BARANGAY = "barangay";
    public static final String COL_EST_MUNICIPALITY = "municipality";
    public static final String COL_EST_PROVINCE = "province";
    public static final String COL_EST_REGION = "region";
    public static final String COL_EST_DATE_AND_TIME = "date_and_time";



    public DBHandler(Context context) {
        super(context, "bbpd", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Collector Account Table
        String createCollectorAccountTable = "CREATE TABLE " + TABLE_COLLECTOR_ACCOUNT + " (" +
                COLA_ID + " INTEGER DEFAULT NULL, " +
                COLA_NAME + " TEXT DEFAULT NULL, " +
                COLA_ADDRESS + " TEXT DEFAULT NULL, " +
                COLA_PNUMBER + " TEXT DEFAULT NULL, " +
                COLA_USERNAME + " TEXT DEFAULT NULL, " +
                COLA_PASSWORD + " TEXT DEFAULT NULL, " +
                COLA_DATE + " DATETIME NOT NULL DEFAULT current_timestamp);";

        // Create Collections Table
        String createCollectionsTable = "CREATE TABLE " + TABLE_COLLECTIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_IMAGE_PATH + " TEXT DEFAULT NULL, " +
                COL_COLLECTOR_ID + " INTEGER DEFAULT NULL, " +
                COL_ESTABLISHMENT_ID + " INTEGER DEFAULT NULL, " +
                COL_JUNKSHOP_ID + " INTEGER DEFAULT NULL, " +
                COL_STREET + " TEXT DEFAULT NULL, " +
                COL_BARANGAY + " TEXT DEFAULT NULL, " +
                COL_MUNICIPALITY + " TEXT DEFAULT NULL, " +
                COL_CLASSIFICATION + " INTEGER DEFAULT NULL, " +
                COL_PROVINCE + " TEXT DEFAULT NULL, " +
                COL_REGION + " TEXT DEFAULT NULL, " +
                COL_QUANTITY + " DOUBLE DEFAULT 0.00, " +
                COL_TOTAL_PRICE + " DOUBLE NOT NULL DEFAULT 0.00, " +
                COL_COLOR + " INTEGER DEFAULT NULL, " +
                COL_REPRESENTATIVE + " TEXT DEFAULT NULL, " +
                COL_COLLECTOR + " TEXT DEFAULT NULL, " +
                COL_COLLECTION_DATE + " DATETIME NOT NULL DEFAULT current_timestamp);";

        // Create Junkshop Table
        String createJunkshopTable = "CREATE TABLE " + TABLE_JUNKSHOP + " (" +
                COL_JID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_JUNKSHOP_NAME + " TEXT DEFAULT NULL, " +
                COL_JUNKSHOP_IMAGE_PATH + " TEXT DEFAULT NULL, " +
                COL_JUNKSHOP_IN_CHARGE + " TEXT DEFAULT NULL, " +
                COL_JUNKSHOP_STREET + " TEXT DEFAULT NULL, " +
                COL_JUNKSHOP_BARANGAY + " TEXT NOT NULL, " +
                COL_JUNKSHOP_MUNICIPALITY + " TEXT NOT NULL, " +
                COL_JUNKSHOP_PROVINCE + " TEXT NOT NULL, " +
                COL_JUNKSHOP_REGION + " TEXT NOT NULL, " +
                COL_JUNKSHOP_DATE_AND_TIME + " DATETIME NOT NULL DEFAULT current_timestamp);";

        // Create Establishment Table
        String createEstablishmentTable = "CREATE TABLE " + TABLE_ESTABLISHMENT + " (" +
                COL_EID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EST_NAME + " TEXT NOT NULL, " +
                COL_EST_IMAGE_PATH + " TEXT DEFAULT NULL, " +
                COL_EST_IN_CHARGE + " TEXT NOT NULL, " +
                COL_EST_TYPE + " INTEGER NOT NULL, " +
                COL_EST_STREET + " TEXT NOT NULL, " +
                COL_EST_BARANGAY + " TEXT NOT NULL, " +
                COL_EST_MUNICIPALITY + " TEXT NOT NULL, " +
                COL_EST_PROVINCE + " TEXT NOT NULL, " +
                COL_EST_REGION + " TEXT NOT NULL, " +
                COL_EST_DATE_AND_TIME + " DATETIME NOT NULL DEFAULT current_timestamp);";

        db.execSQL(createCollectorAccountTable);
        db.execSQL(createCollectionsTable);
        db.execSQL(createJunkshopTable);
        db.execSQL(createEstablishmentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTOR_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JUNKSHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTABLISHMENT);
        onCreate(db);
    }

    // Method to add a new junkshop
    public void addNewOfflineCollector(int id, String name, String address, String pnumber,
                                String username, String password, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if a collector with the same username already exists
        String query = "SELECT * FROM " + TABLE_COLLECTOR_ACCOUNT + " WHERE " + COLA_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        // If no matching username exists, insert the new collector
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLA_ID, id);
            values.put(COLA_NAME, name);
            values.put(COLA_ADDRESS, address);
            values.put(COLA_PNUMBER, pnumber);
            values.put(COLA_USERNAME, username);
            values.put(COLA_PASSWORD, password);
            values.put(COLA_DATE, date);

            db.insert(TABLE_COLLECTOR_ACCOUNT, null, values);
        }

        cursor.close();
        db.close();
    }


    // Method to add a new collection
    public void addCollection(int collectorId, int establishmentId, int junkshopId,
                              String street, String barangay, String municipality, int classification, String province,
                              String region, double quantity, double totalPrice, int color, String representative,
                              String collector, Context con) {

        // Access SharedPreferences using the context

        SharedPreferences sharedPreferences = con.getSharedPreferences("this_preferences", Context.MODE_PRIVATE);
        colid = sharedPreferences.getString("id", "");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_COLLECTOR_ID, Integer.parseInt(colid));
        values.put(COL_ESTABLISHMENT_ID, establishmentId);
        values.put(COL_JUNKSHOP_ID, junkshopId);
        values.put(COL_STREET, street);
        values.put(COL_BARANGAY, barangay);
        values.put(COL_MUNICIPALITY, municipality);
        values.put(COL_CLASSIFICATION, classification);
        values.put(COL_PROVINCE, province);
        values.put(COL_REGION, region);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_TOTAL_PRICE, totalPrice);
        values.put(COL_COLOR, color);
        values.put(COL_REPRESENTATIVE, representative);
        values.put(COL_COLLECTOR, collector);

        try {
            db.insert(TABLE_COLLECTIONS, null, values);
        } catch (SQLiteException e) {
            Log.e("DBHandler", "Error inserting collection data", e);
        } finally {
            db.close();
        }
    }

    public String getPasswordByUsername(String username) {
        String hashedPassword = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    "collector_account",
                    new String[]{"password"},
                    "username = ?",
                    new String[]{username},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                hashedPassword = cursor.getString(cursor.getColumnIndex("password"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return hashedPassword;
    }

    @SuppressLint("Range")
    public String[] getUserDetailsByUsername(String username) {
        String[] userData = null; // Format: {id, name}
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "collector_account",                  // Table name
                    new String[]{"id", "name"},           // Columns to retrieve
                    "username = ?",                       // WHERE clause
                    new String[]{username},               // WHERE clause value
                    null, null, null                      // GroupBy, Having, OrderBy
            );

            if (cursor.moveToFirst()) {
                String userId = cursor.getString(cursor.getColumnIndex("id"));
                String userName = cursor.getString(cursor.getColumnIndex("name"));
                userData = new String[]{userId, userName};
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return userData;
    }

    public void delete_c_record(int id, Context context){
        SQLiteDatabase db = this.getWritableDatabase();
        // Execute delete query
        db.delete(TABLE_COLLECTIONS, COL_ID + " = ?", new String[]{String.valueOf(id)});

        Intent go_to = new Intent(context, NewOfflineRecord.class);
        context.startActivity(go_to);

        db.close();
    }



    public void delete_AllC_record(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Execute delete query to remove all records
        db.delete(TABLE_COLLECTIONS, null, null);

        // Start new activity
        Intent go_to = new Intent(context, NewOfflineRecord.class);
        context.startActivity(go_to);

        db.close();
    }



    public void theCollectorId(String userIdString) {
        col_id =  Integer.parseInt(userIdString);
    }

    public void addNewJunkshopAndCollection(String name, String vendor, int collectorId, int establishment, int junkshopId, String street, String brgy,
                                            String municipality, int classification, String province, String region, double int_quant,
                                            double total_price, int color, String collector, Context con) {

        String jid = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    "junkshop",
                    new String[]{"id"},
                    "name = ?",
                    new String[]{name},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                jid = cursor.getString(cursor.getColumnIndex("id"));


                addCollection(col_id, 0, Integer.parseInt(jid),
                street, brgy, municipality, 0, province,
                        region, int_quant, total_price, color, vendor,
                        collector, con);

            } else {
                SQLiteDatabase dbwrite = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COL_JUNKSHOP_NAME, name);
                values.put(COL_JUNKSHOP_IN_CHARGE, vendor);
                values.put(COL_JUNKSHOP_STREET, street);
                values.put(COL_JUNKSHOP_BARANGAY, brgy);
                values.put(COL_JUNKSHOP_MUNICIPALITY, municipality);
                values.put(COL_JUNKSHOP_PROVINCE, province);
                values.put(COL_JUNKSHOP_REGION, region);

                try {
                    dbwrite.insert(TABLE_JUNKSHOP, null, values);
                } catch (SQLiteException e) {
                    Log.e("DBHandler", "Error inserting establishment data", e);
                } finally {

                    db = this.getReadableDatabase();
                    cursor = db.query(
                            "junkshop",
                            new String[]{"id"},
                            "name = ?",
                            new String[]{name},
                            null, null, null
                    );

                    if (cursor.moveToFirst()) {
                        jid = cursor.getString(cursor.getColumnIndex("id"));

                        addCollection(col_id, 0, Integer.parseInt(jid),
                                street, brgy, municipality, 0, province,
                                region, int_quant, total_price, color, vendor,
                                collector,con);

                    }

                        db.close();
                }


            }


        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

    }

    public void addNewEstAndCollection(String name, String vendor, int collectorId, int establishment, int junkshopId, String street, String brgy,
                                       String municipality, int classification, String province, String region, double int_quant,
                                       double total_price, int color, String collector, Context con ) {

        String eid = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    "establishment",
                    new String[]{"id"},
                    "name = ?",
                    new String[]{name},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                eid = cursor.getString(cursor.getColumnIndex("id"));


                addCollection(col_id, Integer.parseInt(eid), 0,
                        street, brgy, municipality, classification, province,
                        region, int_quant, total_price, color, vendor,
                        collector, con);

            } else {
                SQLiteDatabase dbwrite = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COL_EST_NAME, name);
                values.put(COL_EST_IN_CHARGE, vendor);
                values.put(COL_EST_TYPE, classification);
                values.put(COL_EST_STREET, street);
                values.put(COL_EST_BARANGAY, brgy);
                values.put(COL_EST_MUNICIPALITY, municipality);
                values.put(COL_EST_PROVINCE, province);
                values.put(COL_EST_REGION, region);

                try {
                    dbwrite.insert(TABLE_ESTABLISHMENT, null, values);
                } catch (SQLiteException e) {
                    Log.e("DBHandler", "Error inserting establishment data", e);
                } finally {

                    db = this.getReadableDatabase();
                    cursor = db.query(
                            "establishment",
                            new String[]{"id"},
                            "name = ?",
                            new String[]{name},
                            null, null, null
                    );

                    if (cursor.moveToFirst()) {
                        eid = cursor.getString(cursor.getColumnIndex("id"));

                        addCollection(col_id, Integer.parseInt(eid), 0,
                                street, brgy, municipality, classification, province,
                                region, int_quant, total_price, color, vendor,
                                collector, con);

                    }

                    db.close();
                }


            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

    }

    public void addNewOfflineJs(int sid, String datum, String datum1, String datum2, String datum3, String datum4, String datum5, String datum6, String datum7, String datum8) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to check if a row with the same name already exists
        String query = "SELECT * FROM " + TABLE_JUNKSHOP + " WHERE " + COL_JUNKSHOP_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{datum});

        // If no matching name exists, insert the new row
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COL_JID, sid);
            values.put(COL_JUNKSHOP_NAME, datum);
            values.put(COL_JUNKSHOP_IMAGE_PATH, datum1);
            values.put(COL_JUNKSHOP_IN_CHARGE, datum2);
            values.put(COL_JUNKSHOP_STREET, datum3);
            values.put(COL_JUNKSHOP_BARANGAY, datum4);
            values.put(COL_JUNKSHOP_MUNICIPALITY, datum5);
            values.put(COL_JUNKSHOP_PROVINCE, datum6);
            values.put(COL_JUNKSHOP_REGION, datum7);
            values.put(COL_JUNKSHOP_DATE_AND_TIME, datum8);

            db.insert(TABLE_JUNKSHOP, null, values);
        }

        cursor.close();
        db.close();
    }

    public void addNewOfflineEst(int sid, String datum, String datum1, String datum2, String datum3, String datum4, String datum5, String datum6, String datum7, String datum8, String datum9) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Query to check if a row with the same name already exists
        String query = "SELECT * FROM " + TABLE_ESTABLISHMENT + " WHERE " + COL_EST_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{datum});

        // If no matching name exists, insert the new row
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COL_EID, sid);
            values.put(COL_EST_NAME, datum);
            values.put(COL_EST_IMAGE_PATH, datum1);
            values.put(COL_EST_IN_CHARGE, datum2);
            values.put(COL_EST_TYPE, datum3);
            values.put(COL_EST_STREET, datum4);
            values.put(COL_EST_BARANGAY, datum5);
            values.put(COL_EST_MUNICIPALITY, datum6);
            values.put(COL_EST_PROVINCE, datum7);
            values.put(COL_EST_REGION, datum8);
            values.put(COL_EST_DATE_AND_TIME, datum9);

            db.insert(TABLE_ESTABLISHMENT, null, values);
        }

        cursor.close();
        db.close();

    }

    @SuppressLint("Range")
    public List<Map<String, String>> getAllJunkshopData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM junkshop", null);


        if (cursor.moveToFirst()) {
            do {
                Map<String, String> data = new HashMap<>();
                data.put("id", cursor.getString(cursor.getColumnIndex(COL_JID)));
                data.put("name", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_NAME)));
                data.put("image_path", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_IMAGE_PATH)));
                data.put("vendor", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_IN_CHARGE)));
                data.put("street", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_STREET)));
                data.put("barangay", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_BARANGAY)));
                data.put("municipality", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_MUNICIPALITY)));
                data.put("province", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_PROVINCE)));
                data.put("region", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_REGION)));
                data.put("date_and_time", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_DATE_AND_TIME)));

                dataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dataList;
    }

    @SuppressLint("Range")
    public List<Map<String, String>> getAllEstData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM establishment", null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> data = new HashMap<>();
                data.put("id", cursor.getString(cursor.getColumnIndex(COL_EID)));
                data.put("name", cursor.getString(cursor.getColumnIndex(COL_EST_NAME)));
                data.put("image_path", cursor.getString(cursor.getColumnIndex(COL_EST_IMAGE_PATH)));
                data.put("vendor", cursor.getString(cursor.getColumnIndex(COL_EST_IN_CHARGE)));
                data.put("type", cursor.getString(cursor.getColumnIndex(COL_EST_TYPE)));
                data.put("street", cursor.getString(cursor.getColumnIndex(COL_EST_STREET)));
                data.put("barangay", cursor.getString(cursor.getColumnIndex(COL_EST_BARANGAY)));
                data.put("municipality", cursor.getString(cursor.getColumnIndex(COL_EST_MUNICIPALITY)));
                data.put("province", cursor.getString(cursor.getColumnIndex(COL_EST_PROVINCE)));
                data.put("region", cursor.getString(cursor.getColumnIndex(COL_EST_REGION)));
                data.put("date_and_time", cursor.getString(cursor.getColumnIndex(COL_EST_DATE_AND_TIME)));

                dataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dataList;
    }

    @SuppressLint("Range")
    public List<Map<String, String>> getAllCollectionData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM collections", null);

        if (cursor.moveToFirst()) {

            do {
                Map<String, String> data = new HashMap<>();
                data.put("id", cursor.getString(cursor.getColumnIndex(COL_ID)));
                data.put("image_path", cursor.getString(cursor.getColumnIndex(COL_IMAGE_PATH)));
                data.put("collector_id", cursor.getString(cursor.getColumnIndex(COL_COLLECTOR_ID)));
                data.put("establishment_id", cursor.getString(cursor.getColumnIndex(COL_ESTABLISHMENT_ID)));
                data.put("junkshop_id", cursor.getString(cursor.getColumnIndex(COL_JUNKSHOP_ID)));
                data.put("street", cursor.getString(cursor.getColumnIndex(COL_STREET)));
                data.put("barangay", cursor.getString(cursor.getColumnIndex(COL_BARANGAY)));
                data.put("municipality", cursor.getString(cursor.getColumnIndex(COL_MUNICIPALITY)));
                data.put("classification", cursor.getString(cursor.getColumnIndex(COL_CLASSIFICATION)));
                data.put("province", cursor.getString(cursor.getColumnIndex(COL_PROVINCE)));
                data.put("region", cursor.getString(cursor.getColumnIndex(COL_REGION)));
                data.put("quantity", cursor.getString(cursor.getColumnIndex(COL_QUANTITY)));
                data.put("total_price", cursor.getString(cursor.getColumnIndex(COL_TOTAL_PRICE)));
                data.put("color", cursor.getString(cursor.getColumnIndex(COL_COLOR)));
                data.put("vendor", cursor.getString(cursor.getColumnIndex(COL_REPRESENTATIVE)));
                data.put("collector", cursor.getString(cursor.getColumnIndex(COL_COLLECTOR)));
                data.put("date_and_time", cursor.getString(cursor.getColumnIndex(COL_COLLECTION_DATE)));
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dataList;
    }


}