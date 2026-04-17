package com.virtualstore.virtualstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.model.Categorie;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Categorie> categories;

    // Interfaces instead of Kotlin lambdas
    public interface OnEditClick {
        void onEdit(Categorie category);
    }

    public interface OnDeleteClick {
        void onDelete(Categorie category);
    }

    private OnEditClick onEdit;
    private OnDeleteClick onDelete;

    public CategoryAdapter(List<Categorie> categories,
                           OnEditClick onEdit,
                           OnDeleteClick onDelete) {
        this.categories = categories;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    // ================= ViewHolder =================
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        Button btnEdit, btnDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvCategoryName);
            btnEdit = itemView.findViewById(R.id.btnEditCategory);
            btnDelete = itemView.findViewById(R.id.btnDeleteCategory);
        }
    }

    // ================= Create View =================
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new CategoryViewHolder(view);
    }

    // ================= Bind Data =================
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Categorie category = categories.get(position);

        holder.tvName.setText(category.getName());

        holder.btnEdit.setOnClickListener(v -> onEdit.onEdit(category));

        holder.btnDelete.setOnClickListener(v -> onDelete.onDelete(category));
    }

    // ================= Size =================
    @Override
    public int getItemCount() {
        return categories.size();
    }

    // ================= Update List =================
    public void updateList(List<Categorie> newList) {
        this.categories = newList;
        notifyDataSetChanged();
    }
}