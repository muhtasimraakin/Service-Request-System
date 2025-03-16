package com.example.srs;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, fullName, phoneNo, address;
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
        }

        TextView textView = findViewById(R.id.login_from_register);
        String text = getString(R.string.already_account_login);

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.register_email);
        editTextPassword = findViewById(R.id.register_password);
        fullName = findViewById(R.id.full_name);
        phoneNo = findViewById(R.id.phone_no);
        address = findViewById(R.id.address);
        buttonReg = findViewById(R.id.register_button);
        textView = findViewById(R.id.login_from_register);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, full_name, phone_no, address_;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                full_name = String.valueOf(fullName.getText());
                phone_no = String.valueOf(phoneNo.getText());
                address_ = String.valueOf(address.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(full_name)){
                    Toast.makeText(Register.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone_no)){
                    Toast.makeText(Register.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(address_)){
                    Toast.makeText(Register.this, "Enter address", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    userID = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("customers").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", full_name);
                                    user.put("email", email);
                                    user.put("phone", phone_no);
                                    user.put("address", address_);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "onSuccess: user Profile is created for"+ userID);
                                        }
                                    });
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}