package com.sungkanngoding.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> {

    private List<Map<String, Object>> exchangeList;
    private OnExchangeClickListener listener;

    public ExchangeAdapter(List<Map<String, Object>> exchangeList) {
        this.exchangeList = exchangeList;
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_exchange, parent, false);
        return new ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
        Map<String, Object> exchange = exchangeList.get(position);
        holder.bind(exchange);

        // Mengatur timestamp
        Long timestamp = (Long) exchange.get("timestamp");
        if (timestamp != null) {
            String formattedDate = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault()).format(new Date(timestamp));
            holder.tvTimestamp.setText(formattedDate);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onExchangeClick(exchange);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exchangeList.size();
    }

    public void setOnExchangeClickListener(OnExchangeClickListener listener) {
        this.listener = listener;
    }

    public interface OnExchangeClickListener {
        void onExchangeClick(Map<String, Object> exchange);
    }

    public static class ExchangeViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimestamp, tvProductName, tvTotalPrice, tvTotalBayar;

        public ExchangeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvTotalBayar = itemView.findViewById(R.id.tvTotalBayar);
        }

        public void bind(Map<String, Object> exchange) {
            String productName = (String) exchange.get("produk_name");
            Long totalPrice = (Long) exchange.get("total_price");
            Long totalBayar = (Long) exchange.get("total_price");

            tvProductName.setText(productName);
            tvTotalPrice.setText(totalPrice != null ? "Rp. " + totalPrice : "");
            tvTotalBayar.setText(totalBayar != null ? "Rp. " + totalBayar : "");
        }
    }
}
