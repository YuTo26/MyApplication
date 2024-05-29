package com.sungkanngoding.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editFullName, editUsername, editBio, editEmail, addPhoneNumber, addHomeAddress;
    private DatabaseReference userReference;
    private String currentUserID;
    private ImageView pic_add_photo_user;
    private Button btnUpdatePhoto, btnBack;
    private Uri photo_location;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    // ActivityResultLauncher untuk memilih gambar dari galeri
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        pic_add_photo_user.setImageURI(selectedImageUri);
                        photo_location = selectedImageUri;
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inisialisasi komponen UI
        editFullName = findViewById(R.id.edit_full_name);
        editUsername = findViewById(R.id.edit_username);
        editBio = findViewById(R.id.edit_bio);
        editEmail = findViewById(R.id.edit_email);
        addPhoneNumber = findViewById(R.id.add_phone_number);
        addHomeAddress = findViewById(R.id.add_home_address);
        pic_add_photo_user = findViewById(R.id.edit_photo_user);
        btnUpdatePhoto = findViewById(R.id.btnUpdatePhotos);
        btnBack = findViewById(R.id.btnBack);

        // Inisialisasi Firebase Auth dan Storage
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("photo_user");

        // Dapatkan referensi ke Firebase Database
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Dapatkan data dari Firebase dan tampilkan di EditText
        userReference.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil data dari snapshot
                    String fullName = dataSnapshot.child("full_name").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();
                    String bio = dataSnapshot.child("bio").getValue().toString();
                    String email = dataSnapshot.child("email_address").getValue().toString();
                    String phoneNumber = dataSnapshot.child("phone_number").getValue().toString();
                    String homeAddress = dataSnapshot.child("home_address").getValue().toString();

                    // Tampilkan data di EditText
                    editFullName.setText(fullName);
                    editUsername.setText(username);
                    editBio.setText(bio);
                    editEmail.setText(email);
                    addPhoneNumber.setText(phoneNumber);
                    addHomeAddress.setText(homeAddress);

                    // Tampilkan foto profil jika tersedia
                    if (dataSnapshot.hasChild("profile_picture_url")) {
                        String photoUrl = dataSnapshot.child("profile_picture_url").getValue().toString();
                        Picasso.get().load(photoUrl).centerCrop().fit().into(pic_add_photo_user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Tangani kegagalan membaca data dari Firebase
                Toast.makeText(EditProfileActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup OnClickListener untuk tombol btnUpdatePhoto
        btnUpdatePhoto.setOnClickListener(v -> findPhoto());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup aktivitas saat ini dan kembali ke aktivitas sebelumnya
            }
        });

        // Setup OnClickListener untuk tombol btnSaveChanges
        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(v -> {
            // Ambil nilai dari setiap EditText
            String fullName = editFullName.getText().toString().trim();
            String username = editUsername.getText().toString().trim();
            String bio = editBio.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String phoneNumber = addPhoneNumber.getText().toString().trim();
            String homeAddress = addHomeAddress.getText().toString().trim();

            // Simpan data ke Firebase
            saveUserData(fullName, username, bio, email, phoneNumber, homeAddress);
        });
    }

    // Metode untuk memilih foto profil baru
    public void findPhoto() {
        Intent pictures = new Intent();
        pictures.setType("image/*");
        pictures.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(pictures);
    }

    // Metode untuk menyimpan data pengguna ke Firebase
    private void saveUserData(String fullName, String username, String bio, String email, String phoneNumber, String homeAddress) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            Map<String, Object> userData = new HashMap<>();
            if (!TextUtils.isEmpty(fullName)) {
                userData.put("full_name", fullName);
            }
            if (!TextUtils.isEmpty(username)) {
                userData.put("username", username);
            }
            if (!TextUtils.isEmpty(bio)) {
                userData.put("bio", bio);
            }
            if (!TextUtils.isEmpty(email)) {
                userData.put("email_address", email);
            }
            if (!TextUtils.isEmpty(phoneNumber)) {
                userData.put("phone_number", phoneNumber);
            }
            if (!TextUtils.isEmpty(homeAddress)) {
                userData.put("home_address", homeAddress);
            }

            if (photo_location != null) {
                StorageReference photoRef = storageReference.child(uid + "." + getFileExtension(photo_location));
                photoRef.putFile(photo_location)
                        .addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String photoUrl = uri.toString();
                            userData.put("profile_picture_url", photoUrl);

                            userReference.child(uid).updateChildren(userData)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditProfileActivity.this, "Perubahan berhasil tersimpan", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EditProfileActivity.this, "Gagal menyimpan perubahan: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }))
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditProfileActivity.this, "Gagal mengunggah foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                userReference.child(uid).updateChildren(userData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Perubahan berhasil tersimpan", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Gagal menyimpan perubahan: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    // Metode untuk mendapatkan ekstensi file dari URI
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
