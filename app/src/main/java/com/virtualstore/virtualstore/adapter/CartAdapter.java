package com.virtualstore.virtualstore.adapter;

import android.view.*;
        import android.widget.*;

        import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartList;
    private CartDAO cartDAO;
    private Runnable onTotalChanged;
    public CartAdapter(List<CartItem> cartList, CartDAO cartDAO, Runnable onTotalChanged) {
        this.cartList = cartList;
        this.cartDAO = cartDAO;
        this.onTotalChanged = onTotalChanged;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty;
        ImageButton plus, minus, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
            qty = itemView.findViewById(R.id.txtQty);
            plus = itemView.findViewById(R.id.btnPlus);
            minus = itemView.findViewById(R.id.btnMinus);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItem item = cartList.get(position);

        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice() + " DH");
        holder.qty.setText(String.valueOf(item.getQuantity()));

        holder.plus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            cartDAO.updateQuantity(item.getId(), item.getQuantity());
            notifyItemChanged(holder.getAdapterPosition());
            onTotalChanged.run();
        });

        holder.minus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                cartDAO.updateQuantity(item.getId(), item.getQuantity());
                notifyItemChanged(holder.getAdapterPosition());
                onTotalChanged.run();
            }
        });

        holder.delete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            cartDAO.deleteItem(item.getId());
            cartList.remove(pos);
            notifyItemRemoved(pos);
            onTotalChanged.run();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}