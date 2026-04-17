package com.virtualstore.virtualstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    // Interfaces instead of Kotlin lambdas
    public interface OnEditClick {
        void onEdit(Order order);
    }

    public interface OnDeleteClick {
        void onDelete(Order order);
    }

    private OnEditClick onEdit;
    private OnDeleteClick onDelete;

    public OrderAdapter(List<Order> orders,
                        OnEditClick onEdit,
                        OnDeleteClick onDelete) {
        this.orders = orders;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    // ================= ViewHolder =================
    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvUser, tvTotal, tvStatus;
        Button btnEdit, btnDelete;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvOrderId);
            tvUser = itemView.findViewById(R.id.tvOrderUser);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);

            btnEdit = itemView.findViewById(R.id.btnEditOrder);
            btnDelete = itemView.findViewById(R.id.btnDeleteOrder);
        }
    }

    // ================= Create View =================
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(view);
    }

    // ================= Bind Data =================
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Order order = orders.get(position);

        holder.tvId.setText("Order #" + order.getId());
        holder.tvUser.setText("Customer: " + order.getUserName());
        holder.tvTotal.setText("Total: " + order.getTotalPrice() + " DH");
        holder.tvStatus.setText("Status: " + order.getStatus());

        holder.btnEdit.setOnClickListener(v -> onEdit.onEdit(order));
        holder.btnDelete.setOnClickListener(v -> onDelete.onDelete(order));
    }

    // ================= Size =================
    @Override
    public int getItemCount() {
        return orders.size();
    }

    // ================= Update List =================
    public void updateList(List<Order> newList) {
        this.orders = newList;
        notifyDataSetChanged();
    }
}