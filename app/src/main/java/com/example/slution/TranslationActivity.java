package com.example.slution;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TranslationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_fragment); // הקובץ שמחלק את המסך ל־2

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_camera, new CameraFragment())
                .replace(R.id.fragment_buffer, new BufferFragment())
                .commit();
    }
}
