package com.example.slution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        initializeFirebase();

        sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);
        setupClickListeners();

        //if (userRef != null) {
            //fetchUserData();
        //}
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
            Toast.makeText(this, "Please log in to access your profile", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void setupClickListeners() {
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> onBackPressed());

        btnApplyChanges.setOnClickListener(v -> {
            if (validateInputs()) {
                showLoading(true);
                updateUserInDatabase();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etFirstName.getText())) {
            etFirstName.setError("First name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etSurname.getText())) {
            etSurname.setError("Last name is required");
            isValid = false;
        }

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
        Log.d(TAG, "Fetching user data...");

        if (userRef == null) {
            Log.e(TAG, "userRef is null, cannot fetch data");
            showLoading(false);
            return;
        }

        loadUserDataFromPreferences();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Firebase data fetched");
                showLoading(false);

                if (snapshot.exists()) {
                    if (snapshot.hasChild("first_name")) {
                        etFirstName.setText(snapshot.child("first_name").getValue(String.class));
                    }
                    if (snapshot.hasChild("surname")) {
                        etSurname.setText(snapshot.child("surname").getValue(String.class));
                    }
                    if (snapshot.hasChild("email")) {
                        etEmail.setText(snapshot.child("email").getValue(String.class));
                    }
                    saveUserDataToPreferences();
                } else if (currentUser != null) {
                    Log.d(TAG, "No data in DB, using FirebaseUser info");
                    String displayName = currentUser.getDisplayName();
                    String email = currentUser.getEmail();

                    if (displayName != null && !displayName.isEmpty()) {
                        String[] parts = displayName.split(" ");
                        if (parts.length > 0) {
                            etFirstName.setText(parts[0]);
                            if (parts.length > 1) {
                                etSurname.setText(parts[parts.length - 1]);
                            }
                        }
                    }

                    if (email != null && !email.isEmpty()) {
                        etEmail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showLoading(false);
                Log.e(TAG, "Database error: " + error.getMessage());
                Snackbar.make(findViewById(android.R.id.content),
                        "Error fetching profile: " + error.getMessage(),
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
        if (userRef == null) {
            showLoading(false);
            Toast.makeText(this, "Cannot update: user not found", Toast.LENGTH_SHORT).show();
            return;
        }

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
                            "Update failed: " + e.getMessage(),
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
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (hasUnsavedChanges()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("You have unsaved changes. Do you want to discard them?")
                    .setPositiveButton("Discard", (dialog, which) -> super.onBackPressed())
                    .setNegativeButton("Stay", (dialog, which) -> dialog.dismiss())
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
