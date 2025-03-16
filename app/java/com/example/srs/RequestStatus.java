package com.example.srs;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RequestStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);

        // Get the request ID and provider name from the intent
        String requestId = getIntent().getStringExtra("requestId");
        String providerUid = getIntent().getStringExtra("providerUid");
        String providerName = getIntent().getStringExtra("providerName");
        String requestStatus = getIntent().getStringExtra("requestStatus");

        // Find the TextViews in the layout and set the request status and provider name
        TextView statusTextView = findViewById(R.id.text_request_status);
        statusTextView.setText("Request Status: " + requestStatus);

        TextView providerNameTextView = findViewById(R.id.text_provider_name);
        providerNameTextView.setText("Provider: " + providerName);

        // Check if the request status is "completed"
        if ("completed".equals(requestStatus)) {
            // If status is "completed", dynamically create a button for rating or reviewing
            Button rateButton = new Button(this);
            rateButton.setText("Rate or Review");
            rateButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            rateButton.setBackgroundResource(R.drawable.rectangle_button); // Set background drawable

            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirect to the RateOrReviewActivity
                    Intent intent = new Intent(RequestStatus.this, RateOrReview.class);
                    intent.putExtra("providerUid", providerUid);
                    startActivity(intent);
                }
            });

            // Find the LinearLayout for buttons
            LinearLayout layoutButtons = findViewById(R.id.layout_buttons);

            // Add the button to the layout
            layoutButtons.addView(rateButton);
        }
    }
}
