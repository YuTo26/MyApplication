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

public class SuccessRegisterActivity extends AppCompatActivity {

    Button btn_explore_now;
    Animation app_splash, bottomtotop, toptobottom;
    ImageView icSuccessRegister;
    TextView tvRegistrasi, tvSubtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success_register);

        //deklrasi find id
        btn_explore_now = findViewById(R.id.btn_explore_now);
        icSuccessRegister = findViewById(R.id.icSuccessRegister);
        tvRegistrasi = findViewById(R.id.tvRegistrasi);
        tvSubtitle = findViewById(R.id.tvSubtitle);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        bottomtotop = AnimationUtils.loadAnimation(this, R.anim.bottomtotop);
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.toptobottom);

        //run_animation
        icSuccessRegister.startAnimation(app_splash);
        btn_explore_now.startAnimation(bottomtotop);
        tvRegistrasi.startAnimation(toptobottom);
        tvSubtitle.startAnimation(toptobottom);

        btn_explore_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohomedashboard = new Intent(SuccessRegisterActivity.this,MainActivity.class);
                startActivity(gotohomedashboard);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}