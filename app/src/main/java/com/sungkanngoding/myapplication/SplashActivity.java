package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    Animation app_splash, bottomtotop;
    ImageView appLogo;
    TextView logoTagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        bottomtotop = AnimationUtils.loadAnimation(this, R.anim.bottomtotop);
        //load element
        appLogo = findViewById(R.id.appLogo);
        logoTagline = findViewById(R.id.logoTagline);

        //run animation
        appLogo.startAnimation(app_splash);
        logoTagline.startAnimation(bottomtotop);

        //setting timer untuk 1 detik pindah activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //memindahkan 1 activity ke activity lain
                Intent gowelcomeone = new Intent(SplashActivity.this,WelcomeOneActivity.class);
                startActivity(gowelcomeone);
                finish();
            }
        }, 2000); // 2000 ms = 2s
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}