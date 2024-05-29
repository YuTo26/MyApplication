package com.sungkanngoding.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterTwoActivity extends AppCompatActivity {

    Button btnBack, btnContinue, btn_add_photo_user;
    ImageView pic_add_photo_user;
    EditText add_full_name, add_bio, phone_number, home_address;
    DatabaseReference reference;

    Uri photo_location;

    ActivityResultLauncher<Intent> activityResultLauncher;

    StorageReference storageReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);
        btn_add_photo_user = findViewById(R.id.btn_add_photo_user);
        pic_add_photo_user = findViewById(R.id.pic_add_photo_user);
        add_full_name = findViewById(R.id.add_full_name);
        add_bio = findViewById(R.id.add_bio);
        phone_number = findViewById(R.id.phone_number);
        home_address = findViewById(R.id.home_address);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("photo_user");

        activityResultLauncher = registerForActivityResult(
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

        btn_add_photo_user.setOnClickListener(v -> findPhoto());

        btnBack.setOnClickListener(v -> {
            Intent backtoregisterone = new Intent(RegisterTwoActivity.this, RegisterOneActivity.class);
            startActivity(backtoregisterone);
        });

        btnContinue.setOnClickListener(v -> {
            btnContinue.setEnabled(false);
            btnContinue.setText("Loading...");
            saveUserData();
        });
    }

    public void findPhoto() {
        Intent pictures = new Intent();
        pictures.setType("image/*");
        pictures.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(pictures);
    }

    private void saveUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String fullName = add_full_name.getText().toString();
            String bio = add_bio.getText().toString();
            String phoneNumber = phone_number.getText().toString();
            String homeAddress = home_address.getText().toString();

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(bio)) {
                Toast.makeText(RegisterTwoActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                btnContinue.setEnabled(true);
                btnContinue.setText("Continue");
                return;
            }

            Map<String, Object> userData = new HashMap<>();
            userData.put("full_name", fullName);
            userData.put("bio", bio);
            userData.put("phone_number", phoneNumber);
            userData.put("home_address", homeAddress);

            if (photo_location != null) {
                StorageReference photoRef = storageReference.child(uid + "." + getFileExtension(photo_location));
                photoRef.putFile(photo_location)
                        .addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String photoUrl = uri.toString();
                            userData.put("profile_picture_url", photoUrl);

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                            reference.updateChildren(userData)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            sendEmailVerification(user);
                                        } else {
                                            Toast.makeText(RegisterTwoActivity.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            btnContinue.setEnabled(true);
                                            btnContinue.setText("Continue");
                                        }
                                    });
                        }))
                        .addOnFailureListener(e -> {
                            Toast.makeText(RegisterTwoActivity.this, "Gagal mengunggah photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            btnContinue.setEnabled(true);
                            btnContinue.setText("Continue");
                        });
            } else {
                reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                reference.updateChildren(userData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                sendEmailVerification(user);
                            } else {
                                Toast.makeText(RegisterTwoActivity.this, "Registrasi gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                btnContinue.setEnabled(true);
                                btnContinue.setText("Continue");
                            }
                        });
            }
        }
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterTwoActivity.this, "Email verifikasi telah dikirim.", Toast.LENGTH_SHORT).show();
                        Intent gotosuccessregister = new Intent(RegisterTwoActivity.this, SuccessRegisterActivity.class);
                        startActivity(gotosuccessregister);
                    } else {
                        Toast.makeText(RegisterTwoActivity.this, "Gagal mengirim email verifikasi.", Toast.LENGTH_SHORT).show();
                    }
                    btnContinue.setEnabled(true);
                    btnContinue.setText("Tersimpan");
                    finish(); // Menutup activity setelah email verifikasi dikirim
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
