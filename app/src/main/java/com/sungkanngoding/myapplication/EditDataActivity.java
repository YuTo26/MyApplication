package com.sungkanngoding.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditDataActivity extends AppCompatActivity {

    private EditText editFullName, editUsernameField, editBio, editEmail, addPhoneNumber, addHomeAddress, editBalance;
    private ImageView imageViewProfile;
    private DatabaseReference userReference;
    private String username;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        // Inisialisasi komponen UI
        editFullName = findViewById(R.id.edit_full_name);
        editUsernameField = findViewById(R.id.edit_username);
        editBio = findViewById(R.id.edit_bio);
        editEmail = findViewById(R.id.edit_email);
        addPhoneNumber = findViewById(R.id.edit_phone_number);
        addHomeAddress = findViewById(R.id.edit_home_address);
        editBalance = findViewById(R.id.edit_balance);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        // Inisialisasi Firebase Auth dan Database Reference
        auth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Ambil username dari Intent
        username = getIntent().getStringExtra("username");

        // Dapatkan data dari Firebase dan tampilkan di EditText
        userReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String fullName = snapshot.child("full_name").getValue(String.class);
                        String usernameFromDb = snapshot.child("username").getValue(String.class);
                        String bio = snapshot.child("bio").getValue(String.class);
                        String email = snapshot.child("email_address").getValue(String.class);
                        String phoneNumber = snapshot.child("phone_number").getValue(String.class);
                        String homeAddress = snapshot.child("home_address").getValue(String.class);
                        String profilePictureUrl = snapshot.child("profile_picture_url").getValue(String.class);
                        Long balance = snapshot.child("balance").getValue(Long.class);

                        // Tampilkan data di EditText
                        editFullName.setText(fullName);
                        editUsernameField.setText(usernameFromDb);
                        editBio.setText(bio);
                        editEmail.setText(email);
                        addPhoneNumber.setText(phoneNumber);
                        addHomeAddress.setText(homeAddress);
                        if (balance != null) {
                            editBalance.setText(String.valueOf(balance));
                        } else {
                            editBalance.setText("");
                        }

                        // Tampilkan gambar profil jika tersedia
                        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                            Picasso.get().load(profilePictureUrl).centerCrop().fit().into(imageViewProfile);
                        } else {
                            // Jika tidak ada gambar profil, tampilkan placeholder atau kosongkan ImageView
                            imageViewProfile.setImageResource(R.drawable.icon_profilenopic);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditDataActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup OnClickListener untuk tombol btnBack
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup aktivitas saat ini dan kembali ke aktivitas sebelumnya
            }
        });

        // Setup OnClickListener untuk tombol btnSaveChanges
        Button btnSaveChanges = findViewById(R.id.btnUpdateChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil nilai dari setiap EditText
                String fullName = editFullName.getText().toString().trim();
                String bio = editBio.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String phoneNumber = addPhoneNumber.getText().toString().trim();
                String homeAddress = addHomeAddress.getText().toString().trim();
                String balanceString = editBalance.getText().toString().trim();

                // Validasi inputan
                if (TextUtils.isEmpty(fullName)) {
                    editFullName.setError("Nama lengkap tidak boleh kosong");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("Email tidak boleh kosong");
                    return;
                }

                long balance = 0;
                if (!TextUtils.isEmpty(balanceString)) {
                    try {
                        balance = Long.parseLong(balanceString);
                    } catch (NumberFormatException e) {
                        editBalance.setError("Balance harus berupa angka");
                        return;
                    }
                }

                // Simpan data ke Firebase
                saveUserData(fullName, bio, email, phoneNumber, homeAddress, balance);
            }
        });
    }

    // Metode untuk menyimpan data pengguna ke Firebase
    private void saveUserData(String fullName, String bio, String email, String phoneNumber, String homeAddress, long balance) {
        // Ambil data pengguna dari Firebase berdasarkan username
        userReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();

                        // Buat Map untuk update data pengguna
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("full_name", fullName);
                        userData.put("bio", bio);
                        userData.put("email_address", email);
                        userData.put("phone_number", phoneNumber);
                        userData.put("home_address", homeAddress);
                        userData.put("balance", balance);

                        // Update data pengguna di Firebase
                        userReference.child(uid).updateChildren(userData)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditDataActivity.this, "Perubahan berhasil tersimpan", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditDataActivity.this, "Gagal menyimpan perubahan: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditDataActivity.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
