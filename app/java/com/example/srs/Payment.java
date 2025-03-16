package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class Payment extends AppCompatActivity {

    private Button buttonDebit, buttonCredit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        EditText cardNumber = findViewById(R.id.card_number);
        EditText expiryDate = findViewById(R.id.card_expiry);
        EditText cvv = findViewById(R.id.card_cvv);
        Button submit = findViewById(R.id.buttonSubmit);
        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);

// Example of how to get the selected date and time
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String customerUid = mAuth.getCurrentUser().getUid();
        // Get the provider's name from the intent extras
        String providerName = getIntent().getStringExtra("providerName");
        String providerUid = getIntent().getStringExtra("providerUid");

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isValidCardInfo(cardNumber.getText().toString(), expiryDate.getText().toString(), cvv.getText().toString())) {
                    Intent intent = new Intent(Payment.this, RequestConfirmation.class);

                    intent.putExtra("providerName", providerName);

                    startActivity(intent);

                    // Create a new document under the "requests" collection
                    CollectionReference requestsRef = db.collection("requests");
                    requestsRef.add(new HashMap<String, Object>() {{
                        put("customerUid", customerUid);
                        put("providerUid", providerUid);
                        put("status", "requested");
                    }}).addOnSuccessListener(documentReference -> {
                        // Document added successfully
//                    System.out.println("Request document added with ID: " + documentReference.getId());
                    }).addOnFailureListener(e -> {
                        // Handle any errors
//                    System.out.println("Error adding request document: " + e.getMessage());
                    });

                } else {
                    Toast.makeText(Payment.this, "Invalid card details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean isValidCardInfo(String cardNumber, String expiryDate, String cvv) {
        // Simple validation logic (You should improve it according to your needs)
        return cardNumber.length() == 16 && expiryDate.matches("\\d{2}/\\d{2}") && cvv.length() == 3;
    }

}
