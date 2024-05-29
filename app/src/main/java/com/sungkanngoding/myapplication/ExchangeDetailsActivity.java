package com.sungkanngoding.myapplication;

import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExchangeDetailsActivity extends AppCompatActivity {

    ImageView ivPictProduct;
    TextView tvProductName, tvPriceProduct, tvUserFullName, tvEmailUser, tvDatetime, tvTotalHarga, tvTotalBayar;

    DatabaseReference transactionRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_details);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        ivPictProduct = findViewById(R.id.ivPictProduct);
        tvProductName = findViewById(R.id.tvProductName);
        tvPriceProduct = findViewById(R.id.tvPriceProduct);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        tvEmailUser = findViewById(R.id.tvEmailUser);
        tvDatetime = findViewById(R.id.tvDatetime);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get transaction uid from intent
            String transactionUid = getIntent().getStringExtra("transaction_uid");

            if (transactionUid != null) {
                // Reference to transaction in Firebase
                transactionRef = FirebaseDatabase.getInstance().getReference().child("Penukaran").child(transactionUid);
                transactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String pic_product = dataSnapshot.child("pic_product").getValue(String.class);
                            String product_name = dataSnapshot.child("produk_name").getValue(String.class);
                            Long price_product = dataSnapshot.child("price_product").getValue(Long.class);
                            String userFullName = dataSnapshot.child("full_name").getValue(String.class);
                            String emailUser = dataSnapshot.child("email_address").getValue(String.class);
                            Long datetime = dataSnapshot.child("timestamp").getValue(Long.class);
                            Long tvTotalHargaValue = dataSnapshot.child("total_price").getValue(Long.class);
                            Long tvTotalBayarValue = dataSnapshot.child("total_price").getValue(Long.class);

                            // Load image using Picasso
                            Picasso.get().load(pic_product).into(ivPictProduct);

                            // Format timestamp to desired format
                            if (datetime != null) {
                                Date date = new Date(datetime);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
                                String formattedDate = sdf.format(date);
                                tvDatetime.setText(formattedDate); // Set TextView with formatted datetime
                            }

                            tvProductName.setText(product_name);
                            tvPriceProduct.setText("Rp. " + price_product);
                            tvUserFullName.setText(userFullName);
                            tvEmailUser.setText(emailUser);
                            tvTotalHarga.setText("Rp. " + tvTotalHargaValue);
                            tvTotalBayar.setText("Rp. " + tvTotalBayarValue);
                        } else {
                            Toast.makeText(ExchangeDetailsActivity.this, "Data transaksi tidak ditemukan", Toast.LENGTH_SHORT).show();
                            finish(); // Close activity if transaction data not found
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ExchangeDetailsActivity.this, "Gagal memuat data transaksi", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity if failed to load data
                    }
                });
            } else {
                Toast.makeText(this, "Data transaksi tidak ditemukan", Toast.LENGTH_SHORT).show();
                finish(); // Close activity if transaction UID not provided
            }
        } else {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user not logged in
        }
    }
}

