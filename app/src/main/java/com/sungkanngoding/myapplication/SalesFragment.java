package com.sungkanngoding.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class SalesFragment extends Fragment implements TransactionAdapter.OnTransactionClickListener, ExchangeAdapter.OnExchangeClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference transactionsRef, exchangesRef;
    private RecyclerView transactionRecyclerView, exchangeRecyclerView;
    private TransactionAdapter transactionAdapter;
    private ExchangeAdapter exchangeAdapter;
    private List<Map<String, Object>> transactionList = new ArrayList<>();
    private List<Map<String, Object>> exchangeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        transactionsRef = FirebaseDatabase.getInstance().getReference().child("Transaksi");
        exchangesRef = FirebaseDatabase.getInstance().getReference().child("Penukaran");

        transactionRecyclerView = view.findViewById(R.id.rvTransactions);
        exchangeRecyclerView = view.findViewById(R.id.rvExchanges);

        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exchangeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        transactionAdapter = new TransactionAdapter(transactionList);
        exchangeAdapter = new ExchangeAdapter(exchangeList);

        transactionAdapter.setOnTransactionClickListener(this);
        exchangeAdapter.setOnExchangeClickListener(this);

        transactionRecyclerView.setAdapter(transactionAdapter);
        exchangeRecyclerView.setAdapter(exchangeAdapter);

        loadTransactions();
        loadExchanges();

        return view;
    }

    private void loadTransactions() {
        if (currentUser != null) {
            Query query = transactionsRef.orderByChild("uid").equalTo(currentUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    transactionList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> transactionData = (Map<String, Object>) snapshot.getValue();
                        if (transactionData != null) {
                            transactionData.put("transactionUid", snapshot.getKey());
                            transactionList.add(0, transactionData);
                        }
                    }
                    transactionAdapter.notifyDataSetChanged();
                    transactionRecyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load transactions", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadExchanges() {
        if (currentUser != null) {
            Query query = exchangesRef.orderByChild("uid").equalTo(currentUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    exchangeList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> exchangeData = (Map<String, Object>) snapshot.getValue();
                        if (exchangeData != null) {
                            exchangeData.put("exchangeUid", snapshot.getKey());
                            exchangeList.add(0, exchangeData);
                        }
                    }
                    exchangeAdapter.notifyDataSetChanged();
                    exchangeRecyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load exchanges", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onTransactionClick(Map<String, Object> transactionData) {
        String transactionUid = (String) transactionData.get("transactionUid");
        Intent intent = new Intent(getActivity(), TransactionDetailsActivity.class);
        intent.putExtra("transaction_uid", transactionUid);
        startActivity(intent);
    }

    @Override
    public void onExchangeClick(Map<String, Object> exchangeData) {
        String exchangeUid = (String) exchangeData.get("exchangeUid");
        Intent intent = new Intent(getActivity(), ExchangeDetailsActivity.class);
        intent.putExtra("transaction_uid", exchangeUid);
        startActivity(intent);
    }
}
