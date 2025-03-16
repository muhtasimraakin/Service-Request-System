package com.example.srs;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterProvider extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, fullName, phoneNo, address, price;
    Button buttonReg;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        TextView textView = findViewById(R.id.login_from_register_provider);
        String text = getString(R.string.already_account_login_provider);

        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red));

        // Find the start and end index of "Sign up" in the text
        int startIndex = text.indexOf("Login");
        int endIndex = startIndex + "Login".length();

        // Apply the red color to the "Sign up" text
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the SpannableString to the TextView
        textView.setText(spannableString);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_provider);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.register_email);
        editTextPassword = findViewById(R.id.register_password);
        fullName = findViewById(R.id.full_name_provider);
        phoneNo = findViewById(R.id.phone_no_provider);
        address = findViewById(R.id.address_provider);
        price = findViewById(R.id.price_provider);
        buttonReg = findViewById(R.id.register_button_provider);
        textView = findViewById(R.id.login_from_register_provider);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginProvider.class);
                startActivity(intent);
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, full_name_provider, phone_no_provider, address_provider, price_provider;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                full_name_provider = String.valueOf(fullName.getText());
                phone_no_provider = String.valueOf(phoneNo.getText());
                address_provider = String.valueOf(address.getText());
                price_provider = String.valueOf(price.getText());
                Spinner serviceProviderSpinner = findViewById(R.id.service_provider_spinner);
                String selectedService = serviceProviderSpinner.getSelectedItem().toString();


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterProvider.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterProvider.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(full_name_provider)){
                    Toast.makeText(RegisterProvider.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone_no_provider)){
                    Toast.makeText(RegisterProvider.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(address_provider)){
                    Toast.makeText(RegisterProvider.this, "Enter address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(price_provider)){
                    Toast.makeText(RegisterProvider.this, "Enter price", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterProvider.this, "Provider Account Created.",
                                            Toast.LENGTH_SHORT).show();

                                    userID = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("providers").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", full_name_provider);
                                    user.put("email", email);
                                    user.put("phone", phone_no_provider);
                                    user.put("address", address_provider);
                                    user.put("servicesOffered", selectedService);
                                    user.put("price", price_provider);

                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: user Profile is created for"+ userID);
                                        }
                                    });

                                    Intent intent = new Intent(getApplicationContext(), LoginProvider.class);
                                    startActivity(intent);
                                    finish();



                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterProvider.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
