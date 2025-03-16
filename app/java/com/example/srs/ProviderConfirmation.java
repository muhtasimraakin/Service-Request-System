package com.example.srs;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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

import com.example.srs.ui.providerHome.ProviderHomeFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProviderConfirmation extends AppCompatActivity {

    private String requestUid;
    private String customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_confirmation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receive the requestUid from the intent extras
        requestUid = getIntent().getStringExtra("requestUid");
        customerName = getIntent().getStringExtra("customerName");

        // Display the customerName in the TextView
        TextView customerNameTextView = findViewById(R.id.customerNameTextView);
        customerNameTextView.setText(customerName);

        // Update status to "accepted" when the user clicks the accept button
        Button acceptButton = findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(v -> {
            updateStatus(requestUid, "accepted");
        });

        // Update status to "rejected" when the user clicks the decline button
        Button declineButton = findViewById(R.id.declineButton);
        declineButton.setOnClickListener(v -> {
            updateStatus(requestUid, "rejected");
        });

    }

    private void updateStatus(String requestUid, String status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("requests").document(requestUid)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProviderConfirmation.this, "Request updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProviderConfirmation.this, ProviderHomeFragment.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Update failed
                    Log.e(TAG, "Error updating document", e);
                    Toast.makeText(ProviderConfirmation.this, "Failed to accept request", Toast.LENGTH_SHORT).show();
                });


    }
}