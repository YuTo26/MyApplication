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
import java.util.Locale;
import java.util.Map;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Map<String, Object>> transactionList;
    private OnTransactionClickListener listener;

    public TransactionAdapter(List<Map<String, Object>> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Map<String, Object> transaction = transactionList.get(position);
        holder.bind(transaction);

        // Mengatur timestamp
        Long timestamp = (Long) transaction.get("timestamp");
        if (timestamp != null) {
            String formattedDate = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault()).format(new Date(timestamp));
            holder.tvTimestamp.setText(formattedDate);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionClick(transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void setOnTransactionClickListener(OnTransactionClickListener listener) {
        this.listener = listener;
    }

    public interface OnTransactionClickListener {
        void onTransactionClick(Map<String, Object> transaction);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimestamp, tvProductName, tvTotalProduct, tvTotalPrice, tvTotalRevenue;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvTotalProduct = itemView.findViewById(R.id.tvTotalProduct);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvTotalRevenue = itemView.findViewById(R.id.tvTotalRevenue);
        }

        public void bind(Map<String, Object> transaction) {
            // Bind data to views
            String productName = (String) transaction.get("produk_name");
            Long totalItem = (Long) transaction.get("total_item");
            Long totalPrice = (Long) transaction.get("total_price");
            Long totalRevenue = (Long) transaction.get("total_revenue");

            tvProductName.setText(productName);
            tvTotalProduct.setText(totalItem != null ? totalItem + " Kg" : "");
            tvTotalPrice.setText(totalPrice != null ? "Rp. " + totalPrice : "");
            tvTotalRevenue.setText(totalRevenue != null ? "Rp. " + totalRevenue : "");
        }
    }
}
