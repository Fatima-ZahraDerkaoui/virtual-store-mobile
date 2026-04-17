package com.virtualstore.virtualstore.activities.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.CategoryAdapter;
import com.virtualstore.virtualstore.database.DataManager;
import com.virtualstore.virtualstore.model.Categorie;

public class CategoryListActivity extends BaseAdminActivity {

    private RecyclerView recyclerCategories;
    private FloatingActionButton fabAddCategory;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Categories");
        }

        recyclerCategories = findViewById(R.id.recyclerCategories);
        fabAddCategory = findViewById(R.id.fabAddCategory);

        adapter = new CategoryAdapter(
            DataManager.categories,
            category -> showCategoryDialog(category),
            category -> {
                DataManager.deleteCategory(category.getId());
                adapter.updateList(DataManager.categories);
            }
        );

        recyclerCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategories.setAdapter(adapter);

        fabAddCategory.setOnClickListener(v -> showCategoryDialog(null));
    }

    private void showCategoryDialog(Categorie category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        EditText etName = dialogView.findViewById(R.id.etCategoryName);

        if (category != null) {
            builder.setTitle("Edit Category");
            etName.setText(category.getName());
        } else {
            builder.setTitle("Add Category");
        }

        builder.setView(dialogView);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString();
            if (!name.isEmpty()) {
                if (category == null) {
                    DataManager.addCategory(new Categorie("", name));
                } else {
                    category.setName(name);
                    DataManager.updateCategory(category);
                }
                adapter.updateList(DataManager.categories);
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
