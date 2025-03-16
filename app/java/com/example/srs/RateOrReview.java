package com.example.srs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateOrReview extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_or_review);

        // Initialize UI elements
        ratingBar = findViewById(R.id.ratingBar);
        reviewEditText = findViewById(R.id.reviewEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // Set up click listener for the submit review button
        submitReviewButton.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        // Retrieve rating and review text
        float rating = ratingBar.getRating();
        String review = reviewEditText.getText().toString();

        // Check if both rating and review are empty
        if (rating == 0 && review.isEmpty()) {
            Toast.makeText(this, "Please Rate or Review", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve providerUid from the intent
        String providerUid = getIntent().getStringExtra("providerUid");
        if (providerUid == null) {
            // Handle case where providerUid is not passed correctly
            Toast.makeText(this, "Provider information not available", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        // Update the provider document
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference providerRef = db.collection("providers").document(providerUid);
        providerRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the existing reviews array
                List<String> reviews = (List<String>) documentSnapshot.get("reviews");

                // Add the new review to the array if the review is not empty
                if (!review.isEmpty()) {
                    if (reviews == null) {
                        reviews = new ArrayList<>();
                    }
                    reviews.add(review);
                }

                // Calculate new rating
                Double existingRating = documentSnapshot.getDouble("rating");
                Double newRating = rating != 0 ? ((existingRating != null ? existingRating * reviews.size() : 0) + rating) / (reviews.size() + 1) : existingRating;

                // Update the provider document with the new reviews array and rating
                Map<String, Object> updates = new HashMap<>();
                updates.put("reviews", reviews);
                updates.put("rating", newRating);

                providerRef.update(updates)
                        .addOnSuccessListener(aVoid -> {
                            // Print the total points
                            Toast.makeText(this, "Thank you for your review!", Toast.LENGTH_SHORT).show();
                            // Finish the activity and go back to the previous activity
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure to update reviews
                            Toast.makeText(this, "Failed to update reviews", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Provider document does not exist
                Toast.makeText(this, "Provider not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            // Handle failure to get provider document
            Toast.makeText(this, "Failed to get provider information", Toast.LENGTH_SHORT).show();
        });
    }



}
