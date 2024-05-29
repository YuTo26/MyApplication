package com.sungkanngoding.myapplication;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Map;

public class ReadDataActivity extends AppCompatActivity {

    private EditText full_name, etusername, bio, email, phone_number, home_address, editBalance;
    private ImageView imageViewUser;
    private String username;
    private Map<String, Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data);

        // Initialize EditText fields
        full_name = findViewById(R.id.full_name);
        etusername = findViewById(R.id.etusername);
        bio = findViewById(R.id.bio);
        email = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone_number);
        home_address = findViewById(R.id.home_address);
        editBalance = findViewById(R.id.balance);  // Assuming balance is an EditText
        imageViewUser = findViewById(R.id.imageViewUser);

        // Retrieve data passed from UserListActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            userData = (Map<String, Object>) extras.getSerializable("userData");
            if (userData != null) {
                // Display user data in EditText fields
                full_name.setText((String) userData.get("full_name"));
                etusername.setText(username);
                bio.setText((String) userData.get("bio"));
                email.setText((String) userData.get("email_address"));
                phone_number.setText((String) userData.get("phone_number"));
                home_address.setText((String) userData.get("home_address"));

                // Display balance
                Object balanceObj = userData.get("balance");
                if (balanceObj != null) {
                    String balanceString = String.valueOf(balanceObj);
                    editBalance.setText(balanceString);
                } else {
                    editBalance.setText("");
                }

                // Display profile picture using Picasso
                String profilePictureUrl = (String) userData.get("profile_picture_url");
                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                    Picasso.get().load(profilePictureUrl).centerCrop().fit().into(imageViewUser);
                } else {
                    imageViewUser.setImageResource(R.drawable.icon_profilenopic); // Placeholder image
                }
            }
        }
    }
}
