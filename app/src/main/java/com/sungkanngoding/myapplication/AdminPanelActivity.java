package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminPanelActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button btnSignOut;
    private ImageView photoHomeAdmin;
    private LinearLayout llListUser, llAddUsers, llStatement, llAdminBalanceHistory;
    private TextView adminUsername, adminBio, adminSaldo, tvTotalUsers;

    private BarChart barChartTransaksi; // BarChart untuk Transaksi
    private LineChart lineChartPenukaran; // LineChart untuk Penukaran

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize views
        photoHomeAdmin = findViewById(R.id.photo_home_admin);
        adminUsername = findViewById(R.id.adminUsername);
        adminBio = findViewById(R.id.adminBio);
        adminSaldo = findViewById(R.id.adminSaldo);
        llListUser = findViewById(R.id.llListUser);
        llAddUsers = findViewById(R.id.llAddUsers);
        llStatement = findViewById(R.id.llStatement);
        llAdminBalanceHistory = findViewById(R.id.llAdminBalanceHistory);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        barChartTransaksi = findViewById(R.id.barChartTransaksi);
        lineChartPenukaran = findViewById(R.id.lineChartPenukaran);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Set click listeners for navigation buttons
        llListUser.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, UserListActivity.class);
            startActivity(intent);
        });
        llAddUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, AddUsersActivity.class);
            startActivity(intent);
        });
        llStatement.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, StatementActivity.class);
            startActivity(intent);
        });
        llAdminBalanceHistory.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, AdminBalanceActivity.class);
            startActivity(intent);
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                // Redirect to Login screen
                Intent intent = new Intent(AdminPanelActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Get current user data
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            getUserData(uid);
        } else {
            redirectToLogin();
        }

        // Get total number of users
        getTotalUsers();

        // Load data for both charts
        loadBarChartData(); // Method to load data for BarChart
        loadLineChartData(); // Method to load data for LineChart
    }

    // Method to fetch user data from Firebase
    private void getUserData(String uid) {
        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.child("role").getValue(String.class);
                    if ("admin".equals(role)) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String bio = dataSnapshot.child("bio").getValue(String.class);
                        Long balance = dataSnapshot.child("balance").getValue(Long.class);
                        String photoUrl = dataSnapshot.child("profile_picture_url").getValue(String.class);

                        adminUsername.setText(username);
                        adminBio.setText(bio);
                        adminSaldo.setText(String.valueOf("Rp. " + balance));
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Picasso.get().load(photoUrl).centerCrop().fit().into(photoHomeAdmin);
                        }
                    } else {
                        Toast.makeText(AdminPanelActivity.this, "You are not an admin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminPanelActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPanelActivity.this, "Failed to read user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to fetch total number of users from Firebase
    private void getTotalUsers() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalUsers = (int) dataSnapshot.getChildrenCount();
                tvTotalUsers.setText(String.valueOf(totalUsers));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPanelActivity.this, "Failed to load total users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to load data for BarChart (Transaksi)
    private void loadBarChartData() {
        DatabaseReference transactionRef = FirebaseDatabase.getInstance().getReference("Transaksi");
        ValueEventListener transactionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BarEntry> transactionEntries = new ArrayList<>();
                List<String> transactionLabels = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> transaction = (Map<String, Object>) snapshot.getValue();
                    long timestamp = (long) transaction.get("timestamp");
                    int totalRevenue = ((Long) transaction.get("total_revenue")).intValue();
                    String date = formatDate(timestamp);

                    transactionEntries.add(new BarEntry(transactionEntries.size(), totalRevenue));
                    transactionLabels.add(date);
                }
                drawBarChart(transactionEntries, transactionLabels, "Transaksi");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPanelActivity.this, "Failed to load transaction data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        transactionRef.addValueEventListener(transactionListener);
    }

    // Method to draw BarChart
    private void drawBarChart(List<BarEntry> entries, List<String> labels, String label) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChartTransaksi.setData(barData);

        XAxis xAxis = barChartTransaksi.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = barChartTransaksi.getAxisLeft();
        leftAxis.setGranularity(1f);

        barChartTransaksi.getAxisRight().setEnabled(false);
        barChartTransaksi.getDescription().setEnabled(false);
        barChartTransaksi.animateY(1000);
        barChartTransaksi.invalidate();
    }

    // Method to load data for LineChart (Penukaran)
    private void loadLineChartData() {
        DatabaseReference exchangeRef = FirebaseDatabase.getInstance().getReference("Penukaran");
        ValueEventListener exchangeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Entry> lineEntries = new ArrayList<>();
                List<String> lineLabels = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> exchange = (Map<String, Object>) snapshot.getValue();
                    long timestamp = (long) exchange.get("timestamp");
                    int totalExchanged = ((Long) exchange.get("total_price")).intValue();
                    String date = formatDate(timestamp);

                    lineEntries.add(new Entry(lineEntries.size(), totalExchanged));
                    lineLabels.add(date);
                }
                drawLineChart(lineEntries, lineLabels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminPanelActivity.this, "Failed to load exchange data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        exchangeRef.addValueEventListener(exchangeListener);
    }

    // Method to draw LineChart
    private void drawLineChart(List<Entry> entries, List<String> labels) {
        LineDataSet dataSet = new LineDataSet(entries, "Penukaran");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);

        lineChartPenukaran.setData(lineData);

        XAxis xAxis = lineChartPenukaran.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = lineChartPenukaran.getAxisLeft();
        leftAxis.setGranularity(1f);

        lineChartPenukaran.getAxisRight().setEnabled(false);
        lineChartPenukaran.getDescription().setEnabled(false);
        lineChartPenukaran.animateY(1000);
        lineChartPenukaran.invalidate();
    }

    // Method to format timestamp into date string
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // Method to redirect to login screen
    private void redirectToLogin() {
        Toast.makeText(this, "Silahkan Login lebih dulu", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AdminPanelActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            redirectToLogin();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            redirectToLogin();
        }
    }
}

