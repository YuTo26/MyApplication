package com.sungkanngoding.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class StatementActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView tvTotalTransaksi, tvTotalPenukaran, tvTotalSaldoJual, tvTotalSaldoPenukaran;
    private Button btnPickDate;
    private RecyclerView rvTransaksi, rvPenukaran;
    private ListTransactionAdapter transactionAdapter;
    private ListExchangeAdapter exchangeAdapter;
    private List<Map<String, Object>> transactionList = new ArrayList<>();
    private List<Map<String, Object>> exchangeList = new ArrayList<>();
    private long startDateTimestamp, endDateTimestamp;
    private int totalSaldoTransaksi = 0;
    private int totalSaldoPenukaran = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        // Initialize views
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi);
        tvTotalPenukaran = findViewById(R.id.tvTotalPenukaran);
        tvTotalSaldoJual = findViewById(R.id.tvTotalSaldoJual);
        tvTotalSaldoPenukaran = findViewById(R.id.tvTotalSaldoPenukaran);
        btnPickDate = findViewById(R.id.btnPickDate);
        rvTransaksi = findViewById(R.id.rvTransaksi);
        rvPenukaran = findViewById(R.id.rvPenukaran);

        // Setup LayoutManager for RecyclerViews
        rvTransaksi.setLayoutManager(new LinearLayoutManager(this));
        rvPenukaran.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapters for RecyclerViews
        transactionAdapter = new ListTransactionAdapter(transactionList);
        exchangeAdapter = new ListExchangeAdapter(exchangeList);

        // Connect adapters with RecyclerViews
        rvTransaksi.setAdapter(transactionAdapter);
        rvPenukaran.setAdapter(exchangeAdapter);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set initial date range (e.g., one month ago)
        Calendar calendar = Calendar.getInstance();
        endDateTimestamp = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, -1);
        startDateTimestamp = calendar.getTimeInMillis();

        // Load data from Firebase
        loadTransactionData();
        loadExchangeData();

        // Button click listener for Pick Date
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    // Method to show date picker dialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(StatementActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        startDateTimestamp = calendar.getTimeInMillis();
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        endDateTimestamp = calendar.getTimeInMillis();

                        // Reload data based on selected date range
                        loadTransactionData();
                        loadExchangeData();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    // Method to load transaction data from Firebase
    private void loadTransactionData() {
        Query query = mDatabase.child("Transaksi").orderByChild("timestamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalTransaksi = 0;
                totalSaldoTransaksi = 0;
                transactionList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long timestamp = (long) snapshot.child("timestamp").getValue();

                    // Filter data based on selected date range
                    if (timestamp >= startDateTimestamp && timestamp <= endDateTimestamp) {
                        Map<String, Object> transaction = (Map<String, Object>) snapshot.getValue();
                        transactionList.add(transaction);
                        int totalPendapatan = ((Long) transaction.get("total_revenue")).intValue();
                        totalTransaksi++;
                        totalSaldoTransaksi += totalPendapatan;
                    }
                }

                // Update total transaction display and RecyclerView adapter
                tvTotalTransaksi.setText("Total Transaksi: " + totalTransaksi);
                tvTotalSaldoJual.setText("Saldo Jual: Rp. " + totalSaldoTransaksi);
                transactionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatementActivity.this, "Failed to load transaction data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to load exchange data from Firebase
    private void loadExchangeData() {
        Query query = mDatabase.child("Penukaran").orderByChild("timestamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalPenukaran = 0;
                totalSaldoPenukaran = 0;
                exchangeList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long timestamp = (long) snapshot.child("timestamp").getValue();

                    // Filter data based on selected date range
                    if (timestamp >= startDateTimestamp && timestamp <= endDateTimestamp) {
                        Map<String, Object> exchange = (Map<String, Object>) snapshot.getValue();
                        exchangeList.add(exchange);
                        int totalHarga = ((Long) exchange.get("total_price")).intValue();
                        totalPenukaran++;
                        totalSaldoPenukaran += totalHarga;
                    }
                }

                // Update total exchange display and RecyclerView adapter
                tvTotalPenukaran.setText("Total Penukaran: " + totalPenukaran);
                tvTotalSaldoPenukaran.setText("Saldo Tukar: Rp. " + totalSaldoPenukaran);
                exchangeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatementActivity.this, "Failed to load exchange data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
