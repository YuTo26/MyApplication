package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeOneActivity extends AppCompatActivity {
    Button btnNext, btnSkip;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_one);

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Cek jika pengguna sudah login sebelumnya atau sedang login
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // Arahkan ke MainActivity
            goToMainActivity();
            return; // Exit this activity to prevent further execution
        }

        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonextwelcome = new Intent(WelcomeOneActivity.this, WelcomeTwoActivity.class);
                startActivity(gotonextwelcome);
                finish();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke SignInActivity
                Intent gotoSignIn = new Intent(WelcomeOneActivity.this, SignInActivity.class);
                startActivity(gotoSignIn);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void goToMainActivity() {
        // Arahkan ke MainActivity
        Intent intent = new Intent(WelcomeOneActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
