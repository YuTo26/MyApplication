package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemDetailActivity extends AppCompatActivity {
    Button btnJualSampah, btnBack;
    TextView jenis_sampah, harga_sampah, deskripsi_sampah;
    ImageView pict_item;

    DatabaseReference reference;
    String jenis;
    Long harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_detail);

        // Inisialisasi komponen UI
        jenis_sampah = findViewById(R.id.jenis_sampah);
        harga_sampah = findViewById(R.id.harga_sampah);
        deskripsi_sampah = findViewById(R.id.deskripsi_sampah);
        pict_item = findViewById(R.id.pict_item);
        btnJualSampah = findViewById(R.id.btnJualSampah);
        btnBack = findViewById(R.id.btnBack);

        // Mendapatkan data dari Intent
        Bundle bundle = getIntent().getExtras();
        String new_item_details = bundle.getString("jenis_item");
        Toast.makeText(getApplicationContext(), "Jenis Sampah: " + new_item_details, Toast.LENGTH_SHORT).show();

        // Mengambil data dari Firebase ItemList
        reference = FirebaseDatabase.getInstance().getReference().child("ItemList").child(new_item_details);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    jenis = dataSnapshot.child("jenis_sampah").getValue(String.class);
                    harga = dataSnapshot.child("harga_sampah").getValue(Long.class);  // Ubah disini
                    String deskripsi = dataSnapshot.child("deskripsi_sampah").getValue(String.class);
                    String thumbnailUrl = dataSnapshot.child("url_thumbnail").getValue(String.class);

                    // Set nilai ke TextView
                    jenis_sampah.setText(jenis);
                    if (harga != null) {
                        harga_sampah.setText(String.valueOf("Rp. "+harga+" /Kg"));  // Konversi ke String disini
                    }
                    deskripsi_sampah.setText(deskripsi);

                    // Memuat gambar dari URL thumbnail menggunakan Picasso
                    if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                        Picasso.get().load(thumbnailUrl).centerCrop().fit().into(pict_item);
                    } else {
                        // Jika URL kosong, sembunyikan ImageView
                        pict_item.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

        btnJualSampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototransaction = new Intent(ItemDetailActivity.this, TransactionActivity.class);
                gototransaction.putExtra("jenis_sampah", jenis);
                gototransaction.putExtra("harga_sampah", harga);
                startActivity(gototransaction);

                // Terapkan animasi fade in/out
                overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tutup activity saat ini dan kembali ke activity sebelumnya
                finish();

                // Terapkan animasi fade in/out
                overridePendingTransition(R.anim.fade_in_page, R.anim.fade_out_page);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
