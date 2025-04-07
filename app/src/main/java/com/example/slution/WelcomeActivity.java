package com.example.slution;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MaterialButton btnContinue = findViewById(R.id.btn_continue);
        TextView title = findViewById(R.id.title);
        ImageView image = findViewById(R.id.image_welcome);

        // מסתיר את התמונה והכפתור בתחילה
        image.setVisibility(ImageView.INVISIBLE);
        btnContinue.setVisibility(MaterialButton.INVISIBLE);

        // טוען את האנימציות
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        Animation scaleUpFadeIn = AnimationUtils.loadAnimation(this, R.anim.scale_up_fade_in);
        Animation bounceIn = AnimationUtils.loadAnimation(this, R.anim.bounce_in); // נוסיף מיד

        // מפעיל את האנימציה על הטקסט
        title.startAnimation(slideDown);

        // כשנגמרת אנימציית הטקסט - מתחילה זו של התמונה
        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(ImageView.VISIBLE);
                image.startAnimation(scaleUpFadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        // כשנגמרת אנימציית התמונה - הכפתור קופץ
        scaleUpFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnContinue.setVisibility(MaterialButton.VISIBLE);
                btnContinue.startAnimation(bounceIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        // מעבר למסך הבא
        btnContinue.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
