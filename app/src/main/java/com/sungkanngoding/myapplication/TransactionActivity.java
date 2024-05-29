package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {

    Button btnKirimTransaksi, btnBack, btnPlus, btnMins;
    TextView totalItem, tvTotalHarga, tvTotalBiaya, tvEstimasiPendapatan, tvJenisSampah, tvHargaPerKg;
    Integer jumlahItem = 0;
    Long hargaPerItem;
    String jenisSampah;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference transactionsRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        transactionsRef = FirebaseDatabase.getInstance().getReference().child("Transaksi");

        btnKirimTransaksi = findViewById(R.id.btnKirimTransaksi);
        btnBack = findViewById(R.id.btnBack);
        btnPlus = findViewById(R.id.btnPlus);
        btnMins = findViewById(R.id.btnMins);
        totalItem = findViewById(R.id.totalItem);
        tvTotalHarga = findViewById(R.id.tvHargaItem);
        tvTotalBiaya = findViewById(R.id.tvTotalBiaya);
        tvEstimasiPendapatan = findViewById(R.id.tvTotalPendapatan);
        tvJenisSampah = findViewById(R.id.tvJenisSampah);
        tvHargaPerKg = findViewById(R.id.tvHargaPerKg);

        // Menerima data dari ItemDetailActivity
        Intent intent = getIntent();
        jenisSampah = intent.getStringExtra("jenis_sampah");
        hargaPerItem = intent.getLongExtra("harga_sampah", 0);

        tvJenisSampah.setText(jenisSampah);
        tvHargaPerKg.setText("Rp. " + String.format("%,d", hargaPerItem) + " /Kg");

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlahItem += 1;
                totalItem.setText(String.valueOf(jumlahItem));
                updateCalculations();
                updateButtonsVisibility();
            }
        });

        btnMins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlahItem > 0) {
                    jumlahItem -= 1;
                    totalItem.setText(String.valueOf(jumlahItem));
                    updateCalculations();
                    updateButtonsVisibility();
                }
            }
        });

        btnKirimTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long totalHarga = jumlahItem * hargaPerItem;
                long biayaLayanan = (long) (totalHarga * 0.1); // 10% dari total harga
                long estimasiPendapatan = totalHarga - biayaLayanan;

                // Mendapatkan uid pengguna
                String uid = mAuth.getCurrentUser().getUid();

                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String fullName = dataSnapshot.child("full_name").getValue(String.class);
                        String emailAddress = dataSnapshot.child("email_address").getValue(String.class);
                        Long currentBalance = dataSnapshot.child("balance").getValue(Long.class);

                        // Menyiapkan data transaksi
                        Map<String, Object> transactionData = new HashMap<>();
                        transactionData.put("uid", uid); // Tambahkan uid pengguna
                        transactionData.put("full_name", fullName);
                        transactionData.put("email_address", emailAddress);
                        transactionData.put("produk_name", jenisSampah); // Ubah kunci menjadi "produk_name"
                        transactionData.put("price_product", hargaPerItem);
                        transactionData.put("total_item", jumlahItem);
                        transactionData.put("total_price", totalHarga);
                        transactionData.put("service_fee", biayaLayanan);
                        transactionData.put("total_revenue", estimasiPendapatan);
                        transactionData.put("timestamp", ServerValue.TIMESTAMP); // Tambahkan timestamp

                        DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference().child("Transaksi");
                        transactionReference.push().setValue(transactionData)
                                .addOnSuccessListener(aVoid -> {
                                    // Update saldo user (role user)
                                    long newBalance = currentBalance + estimasiPendapatan;
                                    userReference.child("balance").setValue(newBalance)
                                            .addOnSuccessListener(aVoid1 -> {
                                                // Jika penyimpanan berhasil, update saldo admin
                                                updateAdminBalance(biayaLayanan);

                                                // Mendapatkan UID transaksi untuk SuccessTransactionActivity
                                                transactionReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String transactionUid = "";
                                                        for (DataSnapshot data : snapshot.getChildren()) {
                                                            transactionUid = data.getKey();
                                                        }
                                                        Intent intent = new Intent(TransactionActivity.this, SuccessTransactionActivity.class);
                                                        intent.putExtra("transaction_uid", transactionUid); // Kirim UID transaksi
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(TransactionActivity.this, "Gagal mendapatkan UID transaksi", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            })
                                            .addOnFailureListener(e -> {
                                                // Jika penyimpanan gagal
                                                Toast.makeText(TransactionActivity.this, "Gagal mengupdate saldo", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(TransactionActivity.this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show();
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(TransactionActivity.this, "Gagal mengambil data user", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopreviouspage = new Intent(TransactionActivity.this, ItemDetailActivity.class);
                startActivity(gotopreviouspage);
                overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });
    }

    private void updateAdminBalance(long serviceFee) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("Users");

        adminRef.orderByChild("role").equalTo("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                    Long currentBalance = adminSnapshot.child("balance").getValue(Long.class);
                    if (currentBalance == null) currentBalance = 0L;
                    long newBalance = currentBalance + serviceFee;
                    adminSnapshot.getRef().child("balance").setValue(newBalance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TransactionActivity.this, "Gagal mengupdate saldo admin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCalculations() {
        long totalHarga = jumlahItem * hargaPerItem;
        long biayaLayanan = (long) (totalHarga * 0.1); // 10% dari total harga
        long estimasiPendapatan = totalHarga - biayaLayanan;

        // Menampilkan nilai dalam format mata uang Rupiah
        String totalHargaString = "Rp " + String.format("%,d", totalHarga) + ",00";
        String biayaLayananString = "Rp " + String.format("%,d", biayaLayanan) + ",00";
        String estimasiPendapatanString = "Rp " + String.format("%,d", estimasiPendapatan) + ",00";

        // Menetapkan nilai ke TextView
        tvTotalHarga.setText(totalHargaString);
        tvTotalBiaya.setText(biayaLayananString);
        tvEstimasiPendapatan.setText(estimasiPendapatanString);
    }

    private void updateButtonsVisibility() {
        if (jumlahItem > 0) {
            btnMins.animate().alpha(1).setDuration(300).start();
            btnMins.setEnabled(true);
        } else {
            btnMins.animate().alpha(0).setDuration(300).start();
            btnMins.setEnabled(false);
        }
    }
}
