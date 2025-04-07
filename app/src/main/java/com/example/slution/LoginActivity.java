package com.example.slution;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {

    // Declare views
    private EditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private MaterialTextView tvGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views using their IDs
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoogleLogin = findViewById(R.id.tvGoogleLogin);

        // Set up the login button's click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Validate input
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform login action
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the Google login text's click listener
        tvGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform Google login action
                Toast.makeText(LoginActivity.this, "Google Login Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
