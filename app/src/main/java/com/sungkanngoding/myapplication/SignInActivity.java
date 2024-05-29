package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    Button btnSignIn;
    TextView btnNewAccount, lupaPassword;
    EditText emailAddress, password;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        btnNewAccount = findViewById(R.id.btn_new_account);
        btnSignIn = findViewById(R.id.btn_sign_in);
        emailAddress = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        lupaPassword = findViewById(R.id.lupa_password);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        lupaPassword.setOnClickListener(v -> showPasswordResetDialog());

        btnSignIn.setOnClickListener(v -> handleSignIn());

        btnNewAccount.setOnClickListener(v -> {
            Intent gotoRegisterOne = new Intent(SignInActivity.this, RegisterOneActivity.class);
            startActivity(gotoRegisterOne);
            finishAffinity();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showPasswordResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Masukan email anda");
        builder.setView(input);

        builder.setPositiveButton("Kirim", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignInActivity.this, "Tolong masukan email anda", Toast.LENGTH_SHORT).show();
                return;
            }
            sendPasswordResetEmail(email);
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void sendPasswordResetEmail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Email reset password terkirim", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignInActivity.this, "Gagal mengirim email reset password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleSignIn() {
        btnSignIn.setEnabled(false);
        btnSignIn.setText("Loading...");

        String emailInput = emailAddress.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput)) {
            Toast.makeText(SignInActivity.this, "Tolong lengkapi datanya!", Toast.LENGTH_SHORT).show();
            btnSignIn.setEnabled(true);
            btnSignIn.setText("Sign In");
            return;
        }

        auth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            checkUserRoleByUid(user.getUid());
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(SignInActivity.this, "Autentikasi gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInActivity.this, "Kredensial Invalid. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                        }
                        btnSignIn.setEnabled(true);
                        btnSignIn.setText("Sign In");
                    }
                });
    }

    private void checkUserRoleByUid(String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.child("role").getValue(String.class);
                    if (role != null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userRole", role);
                        editor.apply();

                        if (role.equals("admin")) {
                            Intent intent = new Intent(SignInActivity.this, AdminPanelActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (role.equals("user")) {
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Role not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, "Failed to retrieve role: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
