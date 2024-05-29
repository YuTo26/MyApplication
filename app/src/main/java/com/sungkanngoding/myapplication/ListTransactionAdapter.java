package com.sungkanngoding.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class ListTransactionAdapter extends RecyclerView.Adapter<ListTransactionAdapter.TransactionViewHolder> {

    private List<Map<String, Object>> transactionList;

    public ListTransactionAdapter(List<Map<String, Object>> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_listtransaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Map<String, Object> transaction = transactionList.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFullName, tvTotalRevenue;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvTotalRevenue = itemView.findViewById(R.id.tvTotalRevenue);
        }

        public void bind(Map<String, Object> transaction) {
            String fullName = (String) transaction.get("full_name");
            tvFullName.setText(fullName);
            int totalRevenue = ((Long) transaction.get("total_revenue")).intValue();
            tvTotalRevenue.setText("Rp. " + totalRevenue);
        }
    }
}

