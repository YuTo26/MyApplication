package com.sungkanngoding.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ServiceFeeAdapter extends RecyclerView.Adapter<ServiceFeeAdapter.ServiceFeeViewHolder> {

    private Context context;
    private List<Map<String, Object>> serviceFeeList;

    public ServiceFeeAdapter(Context context, List<Map<String, Object>> serviceFeeList) {
        this.context = context;
        this.serviceFeeList = serviceFeeList;
    }

    @NonNull
    @Override
    public ServiceFeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_service_fee, parent, false);
        return new ServiceFeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceFeeViewHolder holder, int position) {
        Map<String, Object> transaction = serviceFeeList.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return serviceFeeList.size();
    }

    public static class ServiceFeeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductName, tvTotalProduct, tvTotalPrice, tvTotalServiceFee, tvTimestamp;

        public ServiceFeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvTotalProduct = itemView.findViewById(R.id.tvTotalProduct);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvTotalServiceFee = itemView.findViewById(R.id.tvTotalServiceFee);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp); // Initialize timestamp TextView
        }

        public void bind(Map<String, Object> transaction) {
            String productName = (String) transaction.get("produk_name");
            Long totalItem = (Long) transaction.get("total_item");
            Long totalPrice = (Long) transaction.get("total_price");
            Long serviceFee = (Long) transaction.get("service_fee");
            Long timestamp = (Long) transaction.get("timestamp");

            tvProductName.setText(productName);
            tvTotalProduct.setText(totalItem != null ? totalItem + " Kg" : "");
            tvTotalPrice.setText(formatCurrency(totalPrice));
            tvTotalServiceFee.setText(formatCurrency(serviceFee));

            // Format timestamp into readable date/time
            if (timestamp != null) {
                String formattedDate = formatDate(timestamp);
                tvTimestamp.setText(formattedDate);
            }
        }

        private String formatCurrency(Long amount) {
            if (amount == null) return "";
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            return formatter.format(amount);
        }

        private String formatDate(Long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}
