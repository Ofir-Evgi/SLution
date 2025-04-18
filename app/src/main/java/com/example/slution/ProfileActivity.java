package com.example.slution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private EditText etFirstName, etSurname, etBirthdate, etEmail;
    private Button btnApplyChanges;

    private SharedPreferences sharedPreferences;

    // Firebase
    private DatabaseReference userRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etFirstName = findViewById(R.id.edit_first_name);
        etSurname = findViewById(R.id.edit_surname);
        etBirthdate = findViewById(R.id.edit_birthdate);
        etEmail = findViewById(R.id.edit_email);
        btnApplyChanges = findViewById(R.id.btn_apply_changes);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        // קבלת userID מה־Auth
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        loadUserData();

        btnApplyChanges.setOnClickListener(v -> {
            saveUserData();
            updateUserInDatabase();
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
        });

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveUserData();
    }

    private void saveUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("first_name", etFirstName.getText().toString());
        editor.putString("surname", etSurname.getText().toString());
        editor.putString("birthdate", etBirthdate.getText().toString());
        editor.putString("email", etEmail.getText().toString());
        editor.apply();
    }

    private void loadUserData() {
        etFirstName.setText(sharedPreferences.getString("first_name", ""));
        etSurname.setText(sharedPreferences.getString("surname", ""));
        etBirthdate.setText(sharedPreferences.getString("birthdate", ""));
        etEmail.setText(sharedPreferences.getString("email", ""));
    }

    private void updateUserInDatabase() {
        userRef.child("first_name").setValue(etFirstName.getText().toString());
        userRef.child("surname").setValue(etSurname.getText().toString());
        userRef.child("birthdate").setValue(etBirthdate.getText().toString());
        userRef.child("email").setValue(etEmail.getText().toString());
    }
}
