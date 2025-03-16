package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class ProviderInfo extends AppCompatActivity {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView addressTextView;
    private TextView priceTextView;
    private TextView ratingTextView;
    private Button chooseProviderButton;
    private String providerUid;
    // Add a TextView to display the reviews
    private TextView reviewsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_info);

        // Get the provider's name from the intent extras
        String providerName = getIntent().getStringExtra("providerName");

        // Get the UID of the logged-in customer
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String customerUid = mAuth.getCurrentUser().getUid();

        // Get reference to the TextViews
        nameTextView = findViewById(R.id.providerNameTextView);
        emailTextView = findViewById(R.id.providerEmailTextView);
        addressTextView = findViewById(R.id.providerAddressTextView);
        priceTextView = findViewById(R.id.providerPriceTextView);
        ratingTextView = findViewById(R.id.providerRatingTextView);
        chooseProviderButton = findViewById(R.id.chooseProviderButton);
        // Initialize the reviewsTextView
        reviewsTextView = findViewById(R.id.reviewsTextView);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    if (name != null) {
                        String lowercaseName = name.toLowerCase();
                        String lowercaseProviderName = providerName.toLowerCase();

                        if (lowercaseName.contains(lowercaseProviderName)) {
                            String email = document.getString("email");
                            String address = document.getString("address");
//                            Double rating = document.getDouble("rating");

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

                            // Perform null checks before using the values
                            nameTextView.setText("Name: " + name);
                            emailTextView.setText("Email: " + email);
                            addressTextView.setText("Address: " + address);
                            // Set the button text and background
                            if (price != -1) {
                                priceTextView.setText("Price: $" + price); // Include the price in the button text
                            } else {
                                priceTextView.setText("Price: No price found"); // Display a message if no price found
                            }
                            // Set the button text and background
                            if (rating != -1) {
                                ratingTextView.setText("Rating: " + String.format("%.2f", rating)); // Include the price in the button text
                            } else {
                                ratingTextView.setText("Rating: No ratings found"); // Display a message if no price found
                            }

                            // Retrieve the reviews array
                            List<String> reviews = (List<String>) document.get("reviews");

                            // Display reviews if the array is not empty
                            if (reviews != null && !reviews.isEmpty()) {
                                StringBuilder reviewsBuilder = new StringBuilder();
                                String lineSeparator = System.getProperty("line.separator");
                                for (String review : reviews) {
                                    reviewsBuilder.append(review).append(lineSeparator).append(lineSeparator); // Append two new lines for spacing
                                    reviewsBuilder.append("-----------------------------------------------------------\n").append(lineSeparator);
                                }
                                reviewsTextView.setText(reviewsBuilder.toString().trim()); // Set the reviews text to the TextView
                            } else {
                                reviewsTextView.setText("No reviews available"); // Display a message if there are no reviews
                            }

                            providerUid = document.getId();
                            break;
                        }
                    }
                }
                if (nameTextView.getText().toString().isEmpty()) {
                    nameTextView.setText("No provider found with the specified name");
                    emailTextView.setText("");
                    addressTextView.setText("");
                    priceTextView.setText("");
                    ratingTextView.setText("");
                }
            } else {
                nameTextView.setText("Error getting provider information");
                emailTextView.setText("");
                addressTextView.setText("");
                priceTextView.setText("");
                ratingTextView.setText("");
            }
        });


        // Set click listener for the Choose Provider button
        chooseProviderButton.setOnClickListener(v -> {
            // Check if provider information has been loaded
            if (!nameTextView.getText().toString().isEmpty()) {
                // Start the RequestConfirmation activity
                Intent intent = new Intent(ProviderInfo.this, Payment.class);

                // Pass the provider's name as an extra to the Payment activity
                intent.putExtra("providerName", providerName);
                intent.putExtra("providerUid", providerUid);

                startActivity(intent);

            } else {
                Toast.makeText(ProviderInfo.this, "No provider found with the specified name", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
