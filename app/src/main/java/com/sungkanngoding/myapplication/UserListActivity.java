package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewUsers;
    private UserListAdapter userListAdapter;
    private List<String> userList; // List to store usernames
    private Map<String, Map<String, Object>> usersMap; // Map to store user data by username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        usersMap = new HashMap<>();

        userListAdapter = new UserListAdapter(userList, this, new UserListAdapter.OnItemClickListener() {
            @Override
            public void onReadClick(String username, Map<String, Object> userData) {
                // Handle RecyclerView item Read button click here
                Intent intent = new Intent(UserListActivity.this, ReadDataActivity.class);
                intent.putExtra("username", username); // Pass username to ReadDataActivity
                intent.putExtra("userData", new HashMap<>(userData)); // Pass user data to ReadDataActivity
                startActivity(intent);
            }

            @Override
            public void onEditClick(String username, Map<String, Object> userData) {
                // Handle RecyclerView item Edit button click here
                Intent intent = new Intent(UserListActivity.this, EditDataActivity.class);
                intent.putExtra("username", username); // Pass username to EditDataActivity
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(String username, Map<String, Object> userData) {
                // Handle RecyclerView item Delete button click here
                // Penghapusan akan ditangani oleh onClick listener di adapter
            }
        }, usersMap);

        recyclerViewUsers.setAdapter(userListAdapter);

        loadUsers();
    }

    private void loadUsers() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                usersMap.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);
                    String fullName = snapshot.child("full_name").getValue(String.class);
                    String emailAddress = snapshot.child("email_address").getValue(String.class);
                    String bio = snapshot.child("bio").getValue(String.class);
                    String phoneNumber = snapshot.child("phone_number").getValue(String.class);
                    String homeAddress = snapshot.child("home_address").getValue(String.class);
                    String profilePictureUrl = snapshot.child("profile_picture_url").getValue(String.class);
                    Long balance = snapshot.child("balance").getValue(Long.class); // Get balance as Long

                    if (username != null) {
                        userList.add(username); // Add username to the list
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", username);
                        userData.put("role", role != null ? role : "No Role");
                        userData.put("full_name", fullName != null ? fullName : "");
                        userData.put("email_address", emailAddress != null ? emailAddress : "");
                        userData.put("bio", bio != null ? bio : "");
                        userData.put("phone_number", phoneNumber != null ? phoneNumber : "");
                        userData.put("home_address", homeAddress != null ? homeAddress : "");
                        userData.put("profile_picture_url", profilePictureUrl != null ? profilePictureUrl : "");
                        userData.put("balance", balance != null ? balance : 0); // Include balance in userData

                        usersMap.put(username, userData); // Add user data to the map by username
                    }
                }
                userListAdapter.notifyDataSetChanged(); // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserListActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

