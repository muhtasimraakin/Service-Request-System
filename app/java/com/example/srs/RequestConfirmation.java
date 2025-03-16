package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.srs.ui.home.HomeFragment;

public class RequestConfirmation extends AppCompatActivity {

    private static final String TAG = "RequestConfirmation";
    private TextView providerNameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_confirmation);

        // Get the provider's name from the intent extras
        String providerName = getIntent().getStringExtra("providerName");

        // Get reference to the TextView
        providerNameTextView = findViewById(R.id.providerNameTextView);

        if (providerName != null) {
            // Display the message
            providerNameTextView.setText("Request sent to " + providerName);
        } else {
            Log.d(TAG, "Provider name is null");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Assuming you want to go back to a previous activity:
//                finish(); // Closes this activity

                // If you want to start a new activity instead, uncomment the following lines:
                 Intent intent = new Intent(RequestConfirmation.this, MainActivity.class);
                 startActivity(intent);
                 finish();
            }
        }, 10000);
    }

}