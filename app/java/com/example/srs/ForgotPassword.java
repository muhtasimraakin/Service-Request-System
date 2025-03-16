package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button buttonForgot;
    EditText editEmail;
    FirebaseAuth mAuth;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        buttonForgot = findViewById(R.id.forgot_button);
        editEmail = findViewById(R.id.forgot_email);
        mAuth = FirebaseAuth.getInstance();


        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = String.valueOf(editEmail.getText());

                if (!TextUtils.isEmpty(email)) {
                    ResetPassword();
                } else {
                    Toast.makeText(ForgotPassword.this, "Enter email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void ResetPassword(){

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Reset Password link has been sent to Registered Email", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(ForgotPassword.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }
    }
