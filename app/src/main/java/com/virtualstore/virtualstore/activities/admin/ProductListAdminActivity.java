package com.virtualstore.virtualstore.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.ProductAdapter;
import com.virtualstore.virtualstore.database.DataManager;
import com.virtualstore.virtualstore.model.Categorie;
import com.virtualstore.virtualstore.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdminActivity extends BaseAdminActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddProduct;
    private ProductAdapter adapter;
    private String currentQuery = "";
    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_admin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gérer les produits");
        }

        recyclerView = findViewById(R.id.recyclerProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        adapter = new ProductAdapter(
                new ArrayList<>(DataManager.products),
                product -> {
                    // Edit action
                    Intent intent = new Intent(this, AddEditProductActivity.class);
                    intent.putExtra("PRODUCT_ID", product.getId());
                    startActivity(intent);
                },
                product -> {
                    // Delete action
                    new AlertDialog.Builder(this)
                        .setTitle("Supprimer le produit")
                        .setMessage("Voulez-vous vraiment supprimer ce produit ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            DataManager.deleteProduct(product.getId());
                            applyFilters();
                        })
                        .setNegativeButton("Non", null)
                        .show();
                },
                true // isAdminMode
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditProductActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onSearchPerformed(String query) {
        currentQuery = query;
        applyFilters();
    }

    @Override
    public void onFilterClicked() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        for (Categorie cat : DataManager.categories) {
            categories.add(cat.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrer par catégorie");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        builder.setAdapter(arrayAdapter, (dialog, which) -> {
            selectedCategory = categories.get(which);
            applyFilters();
        });
        builder.show();
    }

    private void applyFilters() {
        DataManager.loadAllData();
        List<Product> filteredList = new ArrayList<>();
        for (Product p : DataManager.products) {
            boolean matchesQuery = p.getName() != null && p.getName().toLowerCase().contains(currentQuery.toLowerCase());
            boolean matchesCategory = selectedCategory.equals("All") || (p.getCategory() != null && p.getCategory().equals(selectedCategory));
            
            if (matchesQuery && matchesCategory) {
                filteredList.add(p);
            }
        }
        adapter.updateList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyFilters();
    }
}