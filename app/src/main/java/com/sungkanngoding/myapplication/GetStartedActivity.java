package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GetStartedActivity extends AppCompatActivity {

    Button btnSignUp, btnSignIn;
    Animation toptobottom, slide_from_left, slide_from_right, fade_in;
    ImageView ivGetStarted;
    TextView tvTaglineOne, tvTaglineTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);

        // Load animation
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.toptobottom);
        slide_from_left = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);
        slide_from_right = AnimationUtils.loadAnimation(this, R.anim.slide_from_right);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Load elements
        ivGetStarted = findViewById(R.id.ivGetStarted);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvTaglineOne = findViewById(R.id.tvTaglineOne);
        tvTaglineTwo = findViewById(R.id.tvTaglineTwo);

        // Run animation
        ivGetStarted.startAnimation(toptobottom);
        btnSignUp.startAnimation(slide_from_left);
        btnSignIn.startAnimation(slide_from_right);
        tvTaglineOne.startAnimation(fade_in);
        tvTaglineTwo.startAnimation(fade_in);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosign = new Intent(GetStartedActivity.this, SignInActivity.class);
                startActivity(gotosign);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(GetStartedActivity.this, RegisterOneActivity.class);
                startActivity(gotoregisterone);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
