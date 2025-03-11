package com.example.gbscollectionform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int delayMillis = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("this_preferences", MODE_PRIVATE);

                if(sharedPreferences.contains("username")){
                    Intent form = new Intent(MainActivity.this, Form.class);
                    startActivity(form);

                } else {
                    Intent login = new Intent(MainActivity.this, Login.class);
                    startActivity(login);
                }

                finish();
            }
        }, delayMillis);

    }
}