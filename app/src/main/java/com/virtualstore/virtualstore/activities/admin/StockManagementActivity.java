package com.virtualstore.virtualstore.activities.admin;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.StockAdapter;
import com.virtualstore.virtualstore.database.DataManager;

public class StockManagementActivity extends BaseAdminActivity {

    private RecyclerView recyclerView;
    private StockAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_management);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Stock Management");
        }

        recyclerView = findViewById(R.id.recyclerStock);

        adapter = new StockAdapter(
            DataManager.products,
            (product, newQuantity) -> {
                product.setStockQuantity(newQuantity);
                DataManager.updateProduct(product);
                adapter.updateList(DataManager.products);
            }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
