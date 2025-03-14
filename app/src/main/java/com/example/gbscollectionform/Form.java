package com.example.gbscollectionform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.google.android.material.tabs.TabLayout;

public class Form extends AppCompatActivity {

    //TextView lgt;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayout offline_rec;
    ImageView exit;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Access the SharedPreferences in your next activity
        SharedPreferences sharedPreferences = getSharedPreferences("this_preferences", MODE_PRIVATE);

        // Retrieve the values using the appropriate keys
        String names = sharedPreferences.getString("name", "");
        String userId = sharedPreferences.getString("id", "");
        String username = sharedPreferences.getString("username", "");

        DBHandler dbHandler = new DBHandler(Form.this);
        dbHandler.theCollectorId(userId);

        name = findViewById(R.id.name);

        name.setText(names);

        // Now find the views
        exit = findViewById(R.id.exit);
        offline_rec = findViewById(R.id.offline_rec);

        // Set the onClickListener for the logout button
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear shared preferences and log out
                SharedPreferences sharedPreferences = getSharedPreferences("this_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Start login activity after logging out
                Intent intent = new Intent(Form.this, Login.class);
                startActivity(intent);
                finish(); // Call finish to close the current activity
            }
        });

         offline_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_prev = new Intent(Form.this, NewOfflineRecord.class);
                startActivity(goto_prev);
            }
        });


        // Corrected

        tabLayout = findViewById(R.id.tablayout);
        viewPager =  findViewById(R.id.scroll_container);

        tabLayout.setupWithViewPager(viewPager);

        VPadapter vpAdapter = new VPadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdapter.addFragment(new BarangayForm(), "Barangay");
        vpAdapter.addFragment(new JunkshopsForm(), "Junkshops");
        vpAdapter.addFragment(new LGUForm(), "LGU");
        vpAdapter.addFragment(new PartnersForm(), "Partner");
        RequestQueue requestQueue;

        viewPager.setAdapter(vpAdapter);

    }

    @Override
    public void onBackPressed() {}


}