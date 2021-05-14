package com.example.healthandfitnessapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthandfitnessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;
    private TextView login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        username = findViewById(R.id.inputUserName);
        email = findViewById(R.id.inputEmailRegister);
        password = findViewById(R.id.inputPasswordRegister);
        confirmPassword = findViewById(R.id.inputCPasswordRegister);
        registerButton = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginView);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                String txtCPassword = confirmPassword.getText().toString();

                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtEmail)
                        || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtCPassword)) {
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtCPassword.length() < 6 || !txtCPassword.equals(txtPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password too short or invalid password!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtEmail, txtPassword);
                }
            }
        });
    }

    private void registerUser(final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}