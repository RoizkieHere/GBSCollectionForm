package com.example.gbscollectionform;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.mindrot.jbcrypt.BCrypt;


import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextView login;
    EditText username;
    EditText password;
    TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Properly initialize the views
        login = findViewById(R.id.loginbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        err = findViewById(R.id.err);

        get_credentials();
        get_establishment();
        get_junkshop();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Please fill out the boxes complete before submitting!", Toast.LENGTH_SHORT).show();
                } else {
                    login_method();
                }

            }
        });
    }

    private void login_method() {
        // Get the entered username and password
        String usernameString = username.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        // Initialize the database handler
        DBHandler dbHandler = new DBHandler(Login.this);

        // Check if the username exists and retrieve the hashed password
        String storedHashedPassword = dbHandler.getPasswordByUsername(usernameString);

        // Validate the retrieved hashed password
        if (storedHashedPassword == null) {
            // Username not found
            Toast.makeText(Login.this, "Username not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Adjust hash format if necessary
        if (storedHashedPassword.startsWith("$2y$")) {
            storedHashedPassword = storedHashedPassword.replace("$2y$", "$2a$");
        }

        try {
            // Verify the entered password with the stored hashed password
            if (BCrypt.checkpw(passwordString, storedHashedPassword)) {
                // Password matches, retrieve name and id
                String[] userData = dbHandler.getUserDetailsByUsername(usernameString);

                if (userData != null) {
                    String userId = userData[0];
                    String name = userData[1];

                    SharedPreferences sharedPreferences = getSharedPreferences("this_preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("name", name);
                    editor.putString("id", userId);
                    editor.putString("username", usernameString);
                    editor.apply();

                    dbHandler.theCollectorId(userId);

                    Intent intent = new Intent(Login.this, Form.class);
                    startActivity(intent);
                }
            } else {
                // Password does not match
                Toast.makeText(Login.this, "Invalid password!", Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid hash format or other issues
            Toast.makeText(Login.this, "Error verifying password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void get_junkshop() {

        String url = "https://bbpdbicol.com/collection/api/get_junkshop.php";
        // Initialize the request queue
        RequestQueue queue = Volley.newRequestQueue(this, new CustomHurlStack());

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DBHandler dbHandler = new DBHandler(Login.this);

                String[] row = response.split(";;");

                for (String column : row) {
                    String[] data = column.split(";");

                    // Validate data length
                    if (data.length >= 7) {
                        try {
                            int sid = Integer.parseInt(data[0]);
                            dbHandler.addNewOfflineJs(sid, data[1], data[2], data[3],
                                    data[4], data[5], data[6], data[7], data[8], data[9] );
                        } catch (NumberFormatException e) {
                            Log.e("DBHandler", "Error parsing collector ID", e);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e("DBHandler", "Data format error", e);
                        }
                    } else {
                        Log.e("DBHandler", "Invalid data format: " + column);
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        // Add the request to the request queue
        queue.add(sr);

    }

    private void get_credentials() {

        String url = "https://bbpdbicol.com/collection/api/get_credentials.php";
        // Initialize the request queue
        RequestQueue queue = Volley.newRequestQueue(this, new CustomHurlStack());

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DBHandler dbHandler = new DBHandler(Login.this);

                String[] row = response.split(";;");

                for (String column : row) {
                    String[] data = column.split(";");

                    // Validate data length
                    if (data.length >= 7) {
                        try {
                            int sid = Integer.parseInt(data[0]);
                            dbHandler.addNewOfflineCollector(sid, data[1], data[2], data[3],
                                    data[4], data[5], data[6]);
                        } catch (NumberFormatException e) {
                            Log.e("DBHandler", "Error parsing collector ID", e);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e("DBHandler", "Data format error", e);
                        }
                    } else {
                        Log.e("DBHandler", "Invalid data format: " + column);
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        // Add the request to the request queue
        queue.add(sr);

    }

    private void get_establishment() {

        String url = "https://bbpdbicol.com/collection/api/get_establishment.php";

        // Initialize the request queue
        RequestQueue queue = Volley.newRequestQueue(this, new CustomHurlStack());

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DBHandler dbHandler = new DBHandler(Login.this);

                String[] row = response.split(";;");

                for (String column : row) {
                    String[] data = column.split(";");

                    // Validate data length
                    if (data.length >= 7) {
                        try {
                            int sid = Integer.parseInt(data[0]);
                            dbHandler.addNewOfflineEst(sid, data[1], data[2], data[3],
                                    data[4], data[5], data[6], data[7], data[8], data[9], data[10]);
                        } catch (NumberFormatException e) {
                            Log.e("DBHandler", "Error parsing collector ID", e);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.e("DBHandler", "Data format error", e);
                        }
                    } else {
                        Log.e("DBHandler", "Invalid data format: " + column);
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        // Add the request to the request queue
        queue.add(sr);

    }
}