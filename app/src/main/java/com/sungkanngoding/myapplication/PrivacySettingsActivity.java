package com.sungkanngoding.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrivacySettingsActivity extends AppCompatActivity {

    EditText checkUsername, oldPassword, newPassword, confirmNewPassword;
    Button btnUpdatePassword;
    FirebaseAuth auth;

    private static final String TAG = "PrivacySettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);

        checkUsername = findViewById(R.id.checkUsername);
        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        confirmNewPassword = findViewById(R.id.confirm_new_password);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        auth = FirebaseAuth.getInstance();

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengubah tombol menjadi tidak aktif dan mengubah teks menjadi "Loading..."
                btnUpdatePassword.setEnabled(false);
                btnUpdatePassword.setText("Loading...");

                // Panggil method updatePassword()
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String username = checkUsername.getText().toString().trim();
        String oldPwd = oldPassword.getText().toString().trim();
        String newPwd = newPassword.getText().toString().trim();
        String confirmPwd = confirmNewPassword.getText().toString().trim();

        Log.d(TAG, "Username entered: " + username);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmPwd)) {
            Toast.makeText(this, "Tolong lengkapi semua field!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            Toast.makeText(this, "Password baru yang anda masukan tidak sama.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil pengguna yang sedang login
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Tidak ada pengguna yang sedang login.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cek berdasarkan username
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot exists: " + dataSnapshot.exists());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String uidFromUsername = userSnapshot.getKey();
                        if (user.getUid().equals(uidFromUsername)) {
                            // Re-autentikasi pengguna
                            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPwd);

                            user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
                                if (reauthTask.isSuccessful()) {
                                    user.updatePassword(newPwd).addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Simpan password baru ke Firebase Realtime Database
                                            userSnapshot.child("password").getRef().setValue(newPwd);

                                            Toast.makeText(PrivacySettingsActivity.this, "Password berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                                            // Kembali ke halaman sebelumnya atau lakukan aksi yang sesuai
                                            finish();
                                        } else {
                                            Toast.makeText(PrivacySettingsActivity.this, "Gagal memperbarui password.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(PrivacySettingsActivity.this, "Password lama anda salah.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(PrivacySettingsActivity.this, "Username tidak sesuai dengan user saat ini.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(PrivacySettingsActivity.this, "Username tidak terdaftar.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PrivacySettingsActivity.this, "Gagal memverifikasi username.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
