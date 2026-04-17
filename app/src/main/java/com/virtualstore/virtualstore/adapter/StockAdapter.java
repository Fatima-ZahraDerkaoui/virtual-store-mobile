package com.virtualstore.virtualstore.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.model.Product;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<Product> products;

    // Interface instead of Kotlin lambda
    public interface OnUpdateStock {
        void onUpdate(Product product, int newQuantity);
    }

    private OnUpdateStock onUpdateStock;

    public StockAdapter(List<Product> products, OnUpdateStock onUpdateStock) {
        this.products = products;
        this.onUpdateStock = onUpdateStock;
    }

    // ================= ViewHolder =================
    public static class StockViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvStatus, tvQuantity;
        Button btnMinus, btnPlus;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvStockProductName);
            tvStatus = itemView.findViewById(R.id.tvStockStatus);
            tvQuantity = itemView.findViewById(R.id.tvStockQuantity);
            btnMinus = itemView.findViewById(R.id.btnMinusStock);
            btnPlus = itemView.findViewById(R.id.btnPlusStock);
        }
    }

    // ================= Create View =================
    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock, parent, false);

        return new StockViewHolder(view);
    }

    // ================= Bind Data =================
    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {

        Product product = products.get(position);

        holder.tvName.setText(product.getName());
        holder.tvQuantity.setText(String.valueOf(product.getStockQuantity()));

        // Stock status
        if (product.getStockQuantity() < 5) {
            holder.tvStatus.setText("Low Stock!");
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setText("In Stock");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        }

        // Increase stock
        holder.btnPlus.setOnClickListener(v ->
                onUpdateStock.onUpdate(product, product.getStockQuantity() + 1)
        );

        // Decrease stock
        holder.btnMinus.setOnClickListener(v -> {
            if (product.getStockQuantity() > 0) {
                onUpdateStock.onUpdate(product, product.getStockQuantity() - 1);
            }
        });
    }

    // ================= Size =================
    @Override
    public int getItemCount() {
        return products.size();
    }

    // ================= Update List =================
    public void updateList(List<Product> newList) {
        this.products = newList;
        notifyDataSetChanged();
    }
}
