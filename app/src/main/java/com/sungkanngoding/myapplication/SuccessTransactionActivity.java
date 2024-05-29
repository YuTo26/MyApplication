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

public class SuccessTransactionActivity extends AppCompatActivity {

    Button btnCheckDetails, btnMyDashboard;
    ImageView ivSuccessTransaction;
    Animation app_splash, bottomtotop, toptobottom;
    TextView tvSuccessTransaction, tvSubtitle;
    String transactionUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_success_transaction);

        btnCheckDetails = findViewById(R.id.btnCheckDetails);
        btnMyDashboard = findViewById(R.id.btnMyDashboard);
        ivSuccessTransaction = findViewById(R.id.ivSuccessTransaction);
        tvSuccessTransaction = findViewById(R.id.tvSuccessTransaction);
        tvSubtitle = findViewById(R.id.tvSubtitle);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        bottomtotop = AnimationUtils.loadAnimation(this, R.anim.bottomtotop);
        toptobottom = AnimationUtils.loadAnimation(this, R.anim.toptobottom);

        //run_animation
        ivSuccessTransaction.startAnimation(app_splash);
        btnCheckDetails.startAnimation(bottomtotop);
        btnMyDashboard.startAnimation(bottomtotop);
        tvSuccessTransaction.startAnimation(toptobottom);
        tvSubtitle.startAnimation(toptobottom);

        // Mendapatkan UID transaksi dari intent
        transactionUid = getIntent().getStringExtra("transaction_uid");

        btnCheckDetails.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent gototransactiondetails = new Intent(SuccessTransactionActivity.this, TransactionDetailsActivity.class);
                gototransactiondetails.putExtra("transaction_uid", transactionUid); // Kirim UID transaksi ke TransactionDetailsActivity
                startActivity(gototransactiondetails);
            }
        });

        btnMyDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohomedashboard = new Intent(SuccessTransactionActivity.this, MainActivity.class);
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
