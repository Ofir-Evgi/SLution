package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference buttons
        Button newAccountButton = findViewById(R.id.new_account_button);
        Button loginButton = findViewById(R.id.login_button);

        // Set button listeners (example, add logic as needed)
        newAccountButton.setOnClickListener(v -> {
            // Open New Account Activity
            // Intent intent = new Intent(MainActivity.this, NewAccountActivity.class);
            // startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            // Open Login Activity
            // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // startActivity(intent);
        });
    }
}
