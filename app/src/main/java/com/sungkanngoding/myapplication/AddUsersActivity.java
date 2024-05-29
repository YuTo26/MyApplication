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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddUsersActivity extends AppCompatActivity {

    private EditText addFullName, addUsername, addRole, addBalance, addBio, addEmailAddress, addPhoneNumber, addHomeAddress;
    private ImageView imageViewProfile;
    private DatabaseReference userReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);

        // Initialize UI components
        addFullName = findViewById(R.id.add_full_name);
        addUsername = findViewById(R.id.add_username);
        addRole = findViewById(R.id.add_role);
        addBalance = findViewById(R.id.add_balance);
        addBio = findViewById(R.id.add_bio);
        addEmailAddress = findViewById(R.id.add_email);
        addPhoneNumber = findViewById(R.id.add_phone_number);
        addHomeAddress = findViewById(R.id.add_home_address);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Setup OnClickListener for btnSave
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String fullName = addFullName.getText().toString().trim();
                String username = addUsername.getText().toString().trim();
                String role = addRole.getText().toString().trim();
                String balanceString = addBalance.getText().toString().trim();
                String bio = addBio.getText().toString().trim();
                String emailAddress = addEmailAddress.getText().toString().trim();
                String phoneNumber = addPhoneNumber.getText().toString().trim();
                String homeAddress = addHomeAddress.getText().toString().trim();

                // Validate input fields
                if (TextUtils.isEmpty(fullName)) {
                    addFullName.setError("Nama lengkap tidak boleh kosong");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    addUsername.setError("Username tidak boleh kosong");
                    return;
                }

                long balance = 0;
                if (!TextUtils.isEmpty(balanceString)) {
                    try {
                        balance = Long.parseLong(balanceString);
                    } catch (NumberFormatException e) {
                        addBalance.setError("Saldo harus berupa angka");
                        return;
                    }
                }

                // Create User data directly
                Map<String, Object> userData = new HashMap<>();
                userData.put("full_name", fullName);
                userData.put("username", username);
                userData.put("role", role);
                userData.put("balance", balance);
                userData.put("bio", bio);
                userData.put("email_address", emailAddress);
                userData.put("phone_number", phoneNumber);
                userData.put("home_address", homeAddress);

                // Save user data to Firebase
                saveUserData(userData);
            }
        });

        // Setup OnClickListener for btnBack
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close current activity and go back
            }
        });
    }

    // Method to save user data to Firebase
    private void saveUserData(Map<String, Object> userData) {
        // Push new user data to Firebase under "Users" node
        userReference.push().setValue(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddUsersActivity.this, "Pengguna berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        // Clear EditText fields after successful save
                        clearEditTextFields();
                    } else {
                        Toast.makeText(AddUsersActivity.this, "Gagal menambahkan pengguna: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to clear EditText fields after saving user data
    private void clearEditTextFields() {
        addFullName.setText("");
        addUsername.setText("");
        addRole.setText("");
        addBalance.setText("");
        addBio.setText("");
        addEmailAddress.setText("");
        addPhoneNumber.setText("");
        addHomeAddress.setText("");
    }
}
