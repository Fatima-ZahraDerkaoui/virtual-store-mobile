package com.virtualstore.virtualstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }

    public interface OnEditClick {
        void onEdit(Product product);
    }

    public interface OnDeleteClick {
        void onDelete(Product product);
    }

    private List<Product> products;
    private OnProductClickListener productClickListener;
    private OnAddToCartListener cartListener;
    private OnEditClick editListener;
    private OnDeleteClick deleteListener;
    private boolean isAdminMode = false;

    public ProductAdapter(List<Product> products,
                          OnProductClickListener productClickListener,
                          OnAddToCartListener cartListener) {
        this.products = products;
        this.productClickListener = productClickListener;
        this.cartListener = cartListener;
        this.isAdminMode = false;
    }

    public ProductAdapter(List<Product> products,
                          OnEditClick editListener,
                          OnDeleteClick deleteListener,
                          boolean isAdmin) {
        this.products = products;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
        this.isAdminMode = isAdmin;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvCategory;
        View btnAddToCart; 
        View btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View view) {
            super(view);
            imgProduct = view.findViewById(R.id.imgProduct);
            tvName = view.findViewById(R.id.tvProductName);
            tvPrice = view.findViewById(R.id.tvProductPrice);
            tvCategory = view.findViewById(R.id.tvProductCategory);
            btnAddToCart = view.findViewById(R.id.btnAddToCart);
            btnEdit = view.findViewById(R.id.btnEditProduct);
            btnDelete = view.findViewById(R.id.btnDeleteProduct);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isAdminMode ? R.layout.item_product_admin : R.layout.item_product;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.format("%.2f DH", product.getPrice()));

        if (holder.tvCategory != null) {
            holder.tvCategory.setText(product.getCategory());
        }

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgProduct);

        if (!isAdminMode) {
            if (productClickListener != null) {
                holder.itemView.setOnClickListener(v -> productClickListener.onProductClick(product));
            }
            if (cartListener != null && holder.btnAddToCart != null) {
                holder.btnAddToCart.setOnClickListener(v -> cartListener.onAddToCart(product));
            }
        } else {
            if (editListener != null && holder.btnEdit != null) {
                holder.btnEdit.setOnClickListener(v -> editListener.onEdit(product));
            }
            if (deleteListener != null && holder.btnDelete != null) {
                holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(product));
            }
        }
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public void updateList(List<Product> newList) {
        this.products = newList;
        notifyDataSetChanged();
    }
}