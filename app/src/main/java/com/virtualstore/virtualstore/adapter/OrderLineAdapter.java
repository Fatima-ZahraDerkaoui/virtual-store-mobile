package com.virtualstore.virtualstore.adapter;

import android.view.*;
        import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.model.CartItem;

import java.util.List;

public class OrderLineAdapter extends RecyclerView.Adapter<OrderLineAdapter.ViewHolder> {

    private List<CartItem> list;

    public OrderLineAdapter(List<CartItem> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, qty, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtProductName);
            qty = itemView.findViewById(R.id.txtQty);
            price = itemView.findViewById(R.id.txtPrice);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_line_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        CartItem item = list.get(pos);

        h.name.setText(item.getName());
        h.qty.setText("Quantité : " + item.getQuantity());
        h.price.setText((item.getPrice() * item.getQuantity()) + " DH");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}