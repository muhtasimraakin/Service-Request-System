package com.example.srs;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class RequestCompletion extends AppCompatActivity {

    private String requestUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_completion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receive the customer name and status from the intent extras
        String customerName = getIntent().getStringExtra("customerName");
        String status = getIntent().getStringExtra("status");
        requestUid = getIntent().getStringExtra("requestUid");

        // Display the customer name and status in TextViews
        TextView customerNameTextView = findViewById(R.id.customerNameTextView);
        customerNameTextView.setText("Customer Name: " + customerName);

        TextView statusTextView = findViewById(R.id.statusTextView);
        statusTextView.setText("Status: " + status);

        // Set a click listener for the "Complete Request" button
        Button completeRequestButton = findViewById(R.id.completeRequestButton);
        completeRequestButton.setOnClickListener(v -> {
            if (requestUid != null) { // Check if requestUid is not null
                // Update the status to "completed" in Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("requests").document(requestUid)
                        .update("status", "completed")
                        .addOnSuccessListener(aVoid -> {
                            // Update successful
                            Toast.makeText(RequestCompletion.this, "Request completed", Toast.LENGTH_SHORT).show();
                            finish(); // Go back to the previous activity
                        })
                        .addOnFailureListener(e -> {
                            // Update failed
                            Log.e("FirestoreData", "Error updating document", e);
                            Toast.makeText(RequestCompletion.this, "Failed to complete request", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Log.e("FirestoreData", "Request UID is null");
            }
        });
    }
}
