package com.example.slution;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TranslationActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "TranslationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        Toast.makeText(this, "TranslationActivity loaded", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_translation_fragment);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission already granted, loading fragments");
            loadFragments();
        } else {
            Log.d(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted from dialog, loading fragments");
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                loadFragments();
            } else {
                Log.e(TAG, "Permission denied by user");
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void loadFragments() {
        Log.d(TAG, "Loading fragments");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_camera, new CameraFragment())
                .replace(R.id.fragment_buffer, new BufferFragment())
                .commit();
    }
}
