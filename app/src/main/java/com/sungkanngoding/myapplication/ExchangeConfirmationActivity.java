package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ExchangeConfirmationActivity extends AppCompatActivity {

    // UI Components
    ImageView pic_product_exchange;
    TextView product_name, price_product, tvMyBalance, tvTotalHarga, tvSaldoSisa;
    Button btnBack, btnProsesPenukaran;

    // Firebase
    FirebaseAuth mAuth;
    DatabaseReference productReference, userReference, transactionReference;

    // Data
    long userBalance = 0;
    long productPrice = 0;
    String productImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_confirmation);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        pic_product_exchange = findViewById(R.id.pic_product_exchange);
        product_name = findViewById(R.id.product_name);
        price_product = findViewById(R.id.price_product);
        tvMyBalance = findViewById(R.id.tvMyBalance);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        tvSaldoSisa = findViewById(R.id.tvSaldoSisa);
        btnBack = findViewById(R.id.btnBack);
        btnProsesPenukaran = findViewById(R.id.btnProsesPenukaran);

        // Set initial state of the button to disabled
        btnProsesPenukaran.setEnabled(false);

        // Get data from Intent
        Bundle bundle = getIntent().getExtras();
        String list_product_exchange = bundle.getString("jenis_barang");

        // Fetch product data from Firebase
        productReference = FirebaseDatabase.getInstance().getReference().child("ProductExchange").child(list_product_exchange);
        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nama_produk = dataSnapshot.child("product_name").getValue(String.class);
                    productPrice = dataSnapshot.child("price_product").getValue(Long.class);
                    productImageUrl = dataSnapshot.child("pic_product_exchange").getValue(String.class);

                    // Set values to TextViews
                    product_name.setText(nama_produk);
                    price_product.setText(String.valueOf("Rp. " + productPrice));
                    tvTotalHarga.setText(String.valueOf("Rp. " + productPrice));

                    // Load image using Picasso
                    if (productImageUrl != null && !productImageUrl.isEmpty()) {
                        Picasso.get().load(productImageUrl).centerCrop().fit().into(pic_product_exchange);
                    } else {
                        // If no image URL provided, hide ImageView
                        pic_product_exchange.setVisibility(View.GONE);
                    }

                    // Enable the button after loading data
                    btnProsesPenukaran.setEnabled(true);

                    // Update remaining balance after loading product data
                    updateSaldoSisa();
                } else {
                    Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch user data from Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userBalance = dataSnapshot.child("balance").getValue(Long.class);
                        tvMyBalance.setText(String.valueOf("Rp. " + userBalance));
                        // Update remaining balance after loading user data
                        updateSaldoSisa();
                    } else {
                        Toast.makeText(getApplicationContext(), "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data user", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle case where the user is not logged in
            Toast.makeText(getApplicationContext(), "User belum login", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the user is not logged in
        }

        // Back button click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity and return to previous activity
            }
        });

        // Process exchange button click listener
        btnProsesPenukaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userBalance >= productPrice) {
                    // Sufficient balance, proceed with exchange
                    userBalance -= productPrice;
                    userReference.child("balance").setValue(userBalance).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Transaction successful, save transaction data
                            simpanDataTransaksi();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal memperbarui saldo", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Insufficient balance
                    Toast.makeText(getApplicationContext(), "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                    tvSaldoSisa.setText("Rp. 0");
                    tvSaldoSisa.setTextColor(Color.RED);
                }
            }
        });
    }

    private void updateSaldoSisa() {
        if (userBalance >= productPrice) {
            long remainingBalance = userBalance - productPrice;
            tvSaldoSisa.setText(String.valueOf("Rp. " + remainingBalance));
            tvSaldoSisa.setTextColor(getResources().getColor(R.color.greenPrimary));
        } else {
            tvSaldoSisa.setTextColor(Color.RED);
        }
    }

    private void simpanDataTransaksi() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Fetch data from Firebase
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("full_name").getValue(String.class);
                        String emailAddress = dataSnapshot.child("email_address").getValue(String.class);

                        // Prepare transaction data
                        Map<String, Object> transactionData = new HashMap<>();
                        transactionData.put("uid", uid);
                        transactionData.put("full_name", fullName);
                        transactionData.put("email_address", emailAddress);
                        transactionData.put("produk_name", product_name.getText().toString());
                        transactionData.put("price_product", productPrice);
                        transactionData.put("total_price", productPrice);
                        transactionData.put("pic_product", productImageUrl);
                        transactionData.put("timestamp", ServerValue.TIMESTAMP);

                        // Save transaction data to Firebase
                        transactionReference = FirebaseDatabase.getInstance().getReference().child("Penukaran");
                        String transactionKey = transactionReference.push().getKey(); // Get the transaction key
                        transactionReference.child(transactionKey).setValue(transactionData)
                                .addOnSuccessListener(aVoid -> {
                                    // Transaction data saved successfully
                                    Toast.makeText(getApplicationContext(), "Data transaksi tersimpan", Toast.LENGTH_SHORT).show();

                                    // Navigate to success exchange page
                                    Intent gotosuccessexchange = new Intent(ExchangeConfirmationActivity.this, SuccessExchangeActivity.class);
                                    gotosuccessexchange.putExtra("transaction_uid", transactionKey); // Send transaction UID to SuccessExchangeActivity
                                    startActivity(gotosuccessexchange);

                                    // Apply fade in/out animation
                                    overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to save transaction data
                                    Toast.makeText(getApplicationContext(), "Gagal menyimpan data transaksi", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data user", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "User belum login", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the user is not logged in
        }
    }
}
