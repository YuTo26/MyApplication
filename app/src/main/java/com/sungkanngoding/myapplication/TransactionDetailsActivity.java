package com.sungkanngoding.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionDetailsActivity extends AppCompatActivity {

    TextView tvJenisSampah, tvHargaPerKg, tvTotalItem, tvHargaItem, tvTotalBiaya, tvTotalPendapatan;
    TextView tvUserFullName, tvEmailUser, tvDatetime; // Tambahkan TextView untuk menampilkan tanggal waktu
    DatabaseReference transactionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        // Inisialisasi komponen UI
        tvJenisSampah = findViewById(R.id.jenisSampah);
        tvHargaPerKg = findViewById(R.id.hargaSampah);
        tvTotalItem = findViewById(R.id.totalItem);
        tvHargaItem = findViewById(R.id.tvHargaItem);
        tvTotalBiaya = findViewById(R.id.tvTotalBiaya);
        tvTotalPendapatan = findViewById(R.id.tvTotalPendapatan);
        tvUserFullName = findViewById(R.id.userFullName);
        tvEmailUser = findViewById(R.id.emailUser);
        tvDatetime = findViewById(R.id.tvDatetime); // Inisialisasi TextView untuk tanggal waktu

        // Mendapatkan UID transaksi dari intent
        String transactionUid = getIntent().getStringExtra("transaction_uid");

        if (transactionUid != null) {
            // Referensi ke transaksi di Firebase
            transactionRef = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(transactionUid);
            transactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String jenisSampah = dataSnapshot.child("produk_name").getValue(String.class);
                        Long hargaPerItem = dataSnapshot.child("price_product").getValue(Long.class);
                        Integer totalItem = dataSnapshot.child("total_item").getValue(Integer.class);
                        Long totalHarga = dataSnapshot.child("total_price").getValue(Long.class);
                        Long biayaLayanan = dataSnapshot.child("service_fee").getValue(Long.class);
                        Long estimasiPendapatan = dataSnapshot.child("total_revenue").getValue(Long.class);
                        String fullName = dataSnapshot.child("full_name").getValue(String.class);
                        String emailAddress = dataSnapshot.child("email_address").getValue(String.class);
                        Long timestamp = dataSnapshot.child("timestamp").getValue(Long.class);

                        // Format timestamp ke dalam format yang sesuai
                        if (timestamp != null) {
                            Date date = new Date(timestamp);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
                            String formattedDate = sdf.format(date);
                            tvDatetime.setText(formattedDate); // Set TextView dengan tanggal waktu yang diformat
                        }

                        tvJenisSampah.setText(jenisSampah);
                        tvHargaPerKg.setText("Rp. " + hargaPerItem + " /Kg");
                        tvTotalItem.setText(totalItem + " Kg");
                        tvHargaItem.setText("Rp. " + totalHarga);
                        tvTotalBiaya.setText("Rp. " + biayaLayanan);
                        tvTotalPendapatan.setText("Rp. " + estimasiPendapatan);
                        tvUserFullName.setText(fullName);
                        tvEmailUser.setText(emailAddress);
                    } else {
                        Toast.makeText(TransactionDetailsActivity.this, "Transaksi tidak ditemukan", Toast.LENGTH_SHORT).show();
                        finish(); // Tutup activity jika transaksi tidak ditemukan
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TransactionDetailsActivity.this, "Gagal memuat data transaksi", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup activity jika gagal memuat data
                }
            });
        } else {
            Toast.makeText(this, "Transaksi tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish(); // Tutup activity jika tidak ada transaction_uid yang diterima
        }
    }
}
