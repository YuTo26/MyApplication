package com.sungkanngoding.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminBalanceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewServiceFee;
    private ServiceFeeAdapter serviceFeeAdapter;
    private List<Map<String, Object>> serviceFeeList;
    private DatabaseReference transactionsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_balance);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        transactionsRef = FirebaseDatabase.getInstance().getReference().child("Transaksi");

        // Initialize RecyclerView
        recyclerViewServiceFee = findViewById(R.id.recyclerViewServiceFee);
        recyclerViewServiceFee.setLayoutManager(new LinearLayoutManager(this));
        serviceFeeList = new ArrayList<>();
        serviceFeeAdapter = new ServiceFeeAdapter(this, serviceFeeList);
        recyclerViewServiceFee.setAdapter(serviceFeeAdapter);

        // Load service fee data from Firebase Realtime Database
        loadServiceFeeData();
    }

    private void loadServiceFeeData() {
        // Query all data under "Transaksi" without filtering by uid
        Query query = transactionsRef.orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceFeeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> transaction = (Map<String, Object>) snapshot.getValue();
                    if (transaction != null) {
                        serviceFeeList.add(0, transaction); // Insert item at the beginning of the list
                    }
                }
                serviceFeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
