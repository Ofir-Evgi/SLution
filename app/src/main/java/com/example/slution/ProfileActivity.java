package com.example.slution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private TextInputEditText etFirstName, etSurname, etEmail;
    private MaterialButton btnApplyChanges;
    private FrameLayout loadingOverlay;

    private SharedPreferences sharedPreferences;

    // Firebase
    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        initializeViews();

        // Initialize Firebase
        initializeFirebase();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);

        // Set up click listeners
        setupClickListeners();

        // Fetch user data
        fetchUserData();
    }

    private void initializeViews() {
        etFirstName = findViewById(R.id.edit_first_name);
        etSurname = findViewById(R.id.edit_surname);
        etEmail = findViewById(R.id.edit_email);
        btnApplyChanges = findViewById(R.id.btn_apply_changes);
        loadingOverlay = findViewById(R.id.loading_overlay);
    }

    private void initializeFirebase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        } else {
            // User not logged in, redirect to login screen
            Toast.makeText(this, "Please log in to access your profile", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void setupClickListeners() {
        // Back button click listener
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        // Save changes button click listener
        btnApplyChanges.setOnClickListener(v -> {
            if (validateInputs()) {
                showLoading(true);
                updateUserInDatabase();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validate first name
        if (TextUtils.isEmpty(etFirstName.getText())) {
            etFirstName.setError("First name is required");
            isValid = false;
        }

        // Validate last name
        if (TextUtils.isEmpty(etSurname.getText())) {
            etSurname.setError("Last name is required");
            isValid = false;
        }

        // Validate email
        String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            isValid = false;
        }

        return isValid;
    }

    private void fetchUserData() {
        showLoading(true);

        // Try to load data from SharedPreferences first for faster UI rendering
        loadUserDataFromPreferences();

        // Then fetch the latest data from Firebase
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showLoading(false);

                if (dataSnapshot.exists()) {
                    // Update UI with data from Firebase
                    if (dataSnapshot.hasChild("first_name")) {
                        etFirstName.setText(dataSnapshot.child("first_name").getValue(String.class));
                    }

                    if (dataSnapshot.hasChild("surname")) {
                        etSurname.setText(dataSnapshot.child("surname").getValue(String.class));
                    }

                    if (dataSnapshot.hasChild("email")) {
                        etEmail.setText(dataSnapshot.child("email").getValue(String.class));
                    }

                    // Save the freshly fetched data to SharedPreferences
                    saveUserDataToPreferences();
                } else if (currentUser != null) {
                    // No data exists yet, try to get from Google Auth if available
                    String displayName = currentUser.getDisplayName();
                    String email = currentUser.getEmail();

                    if (displayName != null && !displayName.isEmpty()) {
                        String[] nameParts = displayName.split(" ");
                        if (nameParts.length > 0) {
                            etFirstName.setText(nameParts[0]);

                            if (nameParts.length > 1) {
                                etSurname.setText(nameParts[nameParts.length - 1]);
                            }
                        }
                    }

                    if (email != null && !email.isEmpty()) {
                        etEmail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showLoading(false);
                Snackbar.make(findViewById(android.R.id.content),
                        "Error fetching profile data: " + databaseError.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserDataFromPreferences() {
        etFirstName.setText(sharedPreferences.getString("first_name", ""));
        etSurname.setText(sharedPreferences.getString("surname", ""));
        etEmail.setText(sharedPreferences.getString("email", ""));
    }

    private void updateUserInDatabase() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("first_name", etFirstName.getText().toString().trim());
        updates.put("surname", etSurname.getText().toString().trim());
        updates.put("email", etEmail.getText().toString().trim());

        performDatabaseUpdate(updates);
    }

    private void performDatabaseUpdate(Map<String, Object> updates) {
        userRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    saveUserDataToPreferences();
                    Snackbar.make(findViewById(android.R.id.content),
                            "Profile updated successfully",
                            Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Snackbar.make(findViewById(android.R.id.content),
                            "Failed to update profile: " + e.getMessage(),
                            Snackbar.LENGTH_LONG).show();
                });
    }

    private void saveUserDataToPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("first_name", etFirstName.getText().toString().trim());
        editor.putString("surname", etSurname.getText().toString().trim());
        editor.putString("email", etEmail.getText().toString().trim());
        editor.apply();
    }

    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        // Check if there are unsaved changes
        if (hasUnsavedChanges()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("You have unsaved changes. Do you want to discard them?")
                    .setPositiveButton("Discard", (dialog, which) -> {
                        super.onBackPressed();
                    })
                    .setNegativeButton("Stay", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasUnsavedChanges() {
        String savedFirstName = sharedPreferences.getString("first_name", "");
        String savedSurname = sharedPreferences.getString("surname", "");
        String savedEmail = sharedPreferences.getString("email", "");

        String currentFirstName = etFirstName.getText() != null ? etFirstName.getText().toString() : "";
        String currentSurname = etSurname.getText() != null ? etSurname.getText().toString() : "";
        String currentEmail = etEmail.getText() != null ? etEmail.getText().toString() : "";

        return !savedFirstName.equals(currentFirstName) ||
                !savedSurname.equals(currentSurname) ||
                !savedEmail.equals(currentEmail);
    }
}