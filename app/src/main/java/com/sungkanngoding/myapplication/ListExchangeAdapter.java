package com.sungkanngoding.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ListExchangeAdapter extends RecyclerView.Adapter<ListExchangeAdapter.ExchangeViewHolder> {

    private List<Map<String, Object>> exchangeList;

    public ListExchangeAdapter(List<Map<String, Object>> exchangeList) {
        this.exchangeList = exchangeList;
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_listexchange, parent, false);
        return new ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
        Map<String, Object> exchange = exchangeList.get(position);
        holder.bind(exchange);
    }

    @Override
    public int getItemCount() {
        return exchangeList.size();
    }

    public static class ExchangeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFullName, tvProductName, tvTotalPrice;

        public ExchangeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }

        public void bind(Map<String, Object> exchange) {
            tvFullName.setText((String) exchange.get("full_name"));
            tvProductName.setText((String) exchange.get("produk_name"));
            int totalPrice = ((Long) exchange.get("total_price")).intValue();
            tvTotalPrice.setText("Rp. " + totalPrice);
        }
    }
}
