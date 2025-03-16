package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button get_start_button;
    TextView go_to_provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Get the UID of the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String currentUserUid = mAuth.getCurrentUser().getUid();

            // Check if the UID exists in the "customers" collection
            FirebaseFirestore.getInstance().collection("customers").document(currentUserUid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // User is a customer
                            startActivity(new Intent(getApplicationContext(), CustomerNav.class));
                            finish();
                        } else {
                            // Check if the UID exists in the "providers" collection
                            FirebaseFirestore.getInstance().collection("providers").document(currentUserUid).get()
                                    .addOnSuccessListener(providerDocument -> {
                                        if (providerDocument.exists()) {
                                            // User is a provider
                                            startActivity(new Intent(getApplicationContext(), ProviderNav.class));
                                            finish();
                                        } else {
                                            // User is neither a customer nor a provider
                                            // Handle this case as needed
                                        }
                                    });
                        }
                    });
        } else {
            // User is not logged in, show the MainActivity layout
            setContentView(R.layout.activity_main);

            TextView textView = findViewById(R.id.provider_click_here);
            String text = getString(R.string.are_provider);

            SpannableString spannableString = new SpannableString(text);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red));

            // Find the start and end index of "Sign up" in the text
            int startIndex = text.indexOf("Click Here");
            int endIndex = startIndex + "Click Here".length();

            // Apply the red color to the "Sign up" text
            spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set the SpannableString to the TextView
            textView.setText(spannableString);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            get_start_button = findViewById(R.id.get_started);
            go_to_provider = findViewById(R.id.provider_click_here);

            get_start_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            });

            go_to_provider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), LoginProvider.class);
                    startActivity(intent);
                }
            });
        }
    }


}