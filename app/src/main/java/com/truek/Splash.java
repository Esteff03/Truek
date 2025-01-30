package com.truek;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler().postDelayed(() -> {
            startAnimationSequence();
        }, 600);
    }

    private void startAnimationSequence() {
        ImageView bubbleTopLeft = findViewById(R.id.bubble_top_left);
        ImageView bubbleBottomLeft = findViewById(R.id.bubble_bottom_left);
        ImageView bubbleTopRight = findViewById(R.id.bubble_top_right);
        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.app_name);

        bubbleTopLeft.setVisibility(View.VISIBLE);
        bubbleTopLeft.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));

        new Handler().postDelayed(() -> {
            bubbleBottomLeft.setVisibility(View.VISIBLE);
            bubbleBottomLeft.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        }, 500);

        new Handler().postDelayed(() -> {
            bubbleTopRight.setVisibility(View.VISIBLE);
            bubbleTopRight.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        }, 1000);

        new Handler().postDelayed(() -> {
            logo.setVisibility(View.VISIBLE);
            logo.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        }, 1500);

        new Handler().postDelayed(() -> {
            appName.setVisibility(View.VISIBLE);
            appName.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in));
        }, 2000);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, MainView.class);
            startActivity(intent);
            finish();
        }, 3500);
    }
}
