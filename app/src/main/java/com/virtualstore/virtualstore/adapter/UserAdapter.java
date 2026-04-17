package com.virtualstore.virtualstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;

    // Interfaces instead of Kotlin lambdas
    public interface OnEditClick {
        void onEdit(User user);
    }

    public interface OnDeleteClick {
        void onDelete(User user);
    }

    private OnEditClick onEdit;
    private OnDeleteClick onDelete;

    public UserAdapter(List<User> users,
                       OnEditClick onEdit,
                       OnDeleteClick onDelete) {
        this.users = users;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    // ================= ViewHolder =================
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvEmail;
        Button btnEdit, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvUserName);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            btnEdit = itemView.findViewById(R.id.btnEditUser);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
        }
    }

    // ================= Create View =================
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new UserViewHolder(view);
    }

    // ================= Bind Data =================
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = users.get(position);

        String fullName = (user.getFirstName() + " " + user.getLastName()).trim();

        if (!fullName.isEmpty()) {
            holder.tvName.setText(fullName);
        } else {
            holder.tvName.setText("No Name");
        }

        holder.tvEmail.setText(user.getEmail());

        holder.btnEdit.setOnClickListener(v -> onEdit.onEdit(user));
        holder.btnDelete.setOnClickListener(v -> onDelete.onDelete(user));
    }

    // ================= Size =================
    @Override
    public int getItemCount() {
        return users.size();
    }

    // ================= Update List =================
    public void updateList(List<User> newList) {
        this.users = newList;
        notifyDataSetChanged();
    }
}