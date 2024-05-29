package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterOneActivity extends AppCompatActivity {

    Button btnContinue, btnBack;
    EditText username, password, email_address;
    FirebaseAuth auth; // Inisialisasi FirebaseAuth
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_one);

        // Inisialisasi find by id
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);

        auth = FirebaseAuth.getInstance(); // Mendapatkan instance FirebaseAuth

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtosignin = new Intent(RegisterOneActivity.this, SignInActivity.class);
                startActivity(backtosignin);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameInput = username.getText().toString().trim();
                final String passwordInput = password.getText().toString().trim();
                final String emailInput = email_address.getText().toString().trim();

                if (usernameInput.isEmpty() || passwordInput.isEmpty() || emailInput.isEmpty()) {
                    Toast.makeText(RegisterOneActivity.this, "Tolong lengkapi datanya!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mendaftarkan pengguna baru dengan email dan password
                auth.createUserWithEmailAndPassword(emailInput, passwordInput)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Jika registrasi berhasil, dapatkan pengguna saat ini
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    // Simpan data pengguna ke Firebase Realtime Database menggunakan UID
                                    saveUserData(user.getUid(), emailInput, usernameInput, passwordInput);
                                    // Pindah ke RegisterTwoActivity untuk melengkapi data pengguna
                                    Intent intent = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                                    startActivity(intent);
                                    finish(); // Mengakhiri aktivitas saat ini
                                }
                            } else {
                                // Jika registrasi gagal, tampilkan pesan kesalahan
                                Toast.makeText(RegisterOneActivity.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Fungsi untuk menyimpan data pengguna ke Firebase Realtime Database
    private void saveUserData(String uid, String email, String username, String password) {
        // Referensi ke node pengguna di Firebase Realtime Database
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        // Membuat objek data pengguna
        Map<String, Object> userData = new HashMap<>();
        userData.put("email_address", email);
        userData.put("username", username);
        userData.put("password", password);
        userData.put("role", "user"); // Default role as user
        userData.put("balance", 0);

        // Menyimpan data ke Firebase Realtime Database
        reference.setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegisterOneActivity.this, "haii "+username, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterOneActivity.this, "gagal menyimpan data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
