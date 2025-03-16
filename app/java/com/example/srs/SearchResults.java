package com.example.srs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.List;

public class SearchResults extends AppCompatActivity {

    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        String serviceSelected = getIntent().getStringExtra("serviceSelected");


        // Get reference to the LinearLayout
        mainLayout = findViewById(R.id.main);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        // Query all documents in the "providers" collection
        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean providersFound = false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the value of the "servicesOffered" field for each document
                    String servicesOfferedString = document.getString("servicesOffered");
                    if (servicesOfferedString != null) {
                        String[] servicesOfferedArray = servicesOfferedString.split(",");
                        ArrayList<String> servicesOffered = new ArrayList<>(Arrays.asList(servicesOfferedArray));
                        if (servicesOffered.contains(serviceSelected)) {
                            // Create and add a button for this document
                            // (remaining code for button creation and addition)
                            // Get the value of the "name" field for each document
                            String name = document.getString("name");
                            Log.d("SearchResults", "Document ID: " + document.getId());

                            // Get the value of the "price" field for each document
                            Object priceObj = document.get("price");
                            double price = 0.0; // Default value if price field is not a valid number or if it doesn't exist
                            if (priceObj != null && priceObj instanceof Number) {
                                price = ((Number) priceObj).doubleValue();
                            } else {
                                price = -1;
                            }

                            // Get the value of the "rating" field for each document
                            Object ratingObj = document.get("rating");
                            double rating = 0.0; // Default value if rating field is not a valid number or if it doesn't exist
                            if (ratingObj != null && ratingObj instanceof Number) {
                                rating = ((Number) ratingObj).doubleValue();
                            } else {
                                rating = -1;
                            }

                            Button button = new Button(SearchResults.this);

// Calculate the width of the button as 90% of the screen width
                            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                            Display display = wm.getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            int screenWidth = size.x;
                            int buttonWidth = (int) (screenWidth * 0.9);

                            // Set the layout parameters for the button
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(buttonWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.gravity = Gravity.CENTER_HORIZONTAL; // Center the button horizontally
                            layoutParams.bottomMargin = 26;

                            // Set the calculated width to the button
                            button.setLayoutParams(layoutParams);

                            // Set the button text and background
                            if (price != -1 && rating != -1) {
                                button.setText(name + "\n $" + price + " : Rating: " + String.format("%.2f", rating)); // Include the price and rating in the button text
                            } else if (price != -1 ) {
                                button.setText(name + "\n $ " + price + ". Rating not available"); // Include the price and indicate no rating
                            } else if (rating != -1) {
                                button.setText(name + "\n No price.  Rating: " + String.format("%.2f", rating)); // Include the rating and indicate no price
                            } else {
                                button.setText(name + "\n No price. Rating not available"); // Indicate no price and no rating
                            }

                            // Create a SpannableString for the button text
//                            SpannableString spannableString = new SpannableString(name + "\n $" + price + " - Rating: " + String.format("%.2f", rating));

                            // Find the index of the "$" symbol in the text
//                            int dollarIndex = spannableString.toString().indexOf("$");
//
//                            // Apply a ForegroundColorSpan to the "$" symbol to make it red
//                            ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
//                            spannableString.setSpan(redColorSpan, dollarIndex, dollarIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                            // Set the button text to the SpannableString
//                            button.setText(spannableString);


                            button.setBackgroundResource(R.drawable.provider_card);


                            // Set click listener for the button
                            button.setOnClickListener(v -> {
                                // Start the Provider_Profile activity, passing the provider's name as an extra
                                startProviderProfileActivity(name);
                            });

                            // Add the button to the LinearLayout
                            mainLayout.addView(button);



                            providersFound = true;
                        }
                    }
                }
                // If no providers are found, display a message
                if (!providersFound) {
                    TextView textView = new TextView(SearchResults.this);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setText("No providers with the selected service at the moment");
                    textView.setGravity(Gravity.CENTER);
                    mainLayout.addView(textView);
                }
            } else {
                // Handle the case when data retrieval fails
                // You can display an error message or handle it in any other appropriate way
            }


        });
    }

    // Method to start Provider_Info activity and pass the provider's name as an extra
    private void startProviderProfileActivity(String providerName) {
        Intent intent = new Intent(this, ProviderInfo.class);
        intent.putExtra("providerName", providerName);
        startActivity(intent);
    }
}

