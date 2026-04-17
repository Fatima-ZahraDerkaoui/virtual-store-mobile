package com.virtualstore.virtualstore.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.OrderAdapter;
import com.virtualstore.virtualstore.database.DataManager;
import com.virtualstore.virtualstore.database.DatabaseHelper;
import com.virtualstore.virtualstore.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseAdminActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private String currentQuery = "";
    private String selectedStatus = "All";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        dbHelper = new DatabaseHelper(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Orders");
        }

        recyclerView = findViewById(R.id.recyclerOrders);

        // Fetch fresh data from DB and update DataManager
        refreshOrderData();

        adapter = new OrderAdapter(
                DataManager.orders,
                order -> {
                    Intent intent = new Intent(this, OrderDetailActivity.class);
                    intent.putExtra("ORDER_ID", order.getId());
                    startActivity(intent);
                },
                order -> {
                    DataManager.deleteOrder(order.getId());
                    refreshOrderData();
                    applyFilters();
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void refreshOrderData() {
        // We ensure DataManager.orders has the real data from DB
        DataManager.orders.clear();
        DataManager.orders.addAll(dbHelper.getAllOrders());
    }

    @Override
    public void onSearchPerformed(String query) {
        currentQuery = query;
        applyFilters();
    }

    @Override
    public void onFilterClicked() {
        String[] statuses = {"All", "Pending", "Shipped", "Delivered", "Cancelled"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by Status");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statuses);
        builder.setAdapter(arrayAdapter, (dialog, which) -> {
            selectedStatus = statuses[which];
            applyFilters();
        });
        builder.show();
    }

    private void applyFilters() {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : DataManager.orders) {
            boolean matchesQuery = (order.getId() != null && order.getId().toLowerCase().contains(currentQuery.toLowerCase())) ||
                                   (order.getUserName() != null && order.getUserName().toLowerCase().contains(currentQuery.toLowerCase()));
            
            boolean matchesStatus = selectedStatus.equals("All") || (order.getStatus() != null && order.getStatus().equals(selectedStatus));
            
            if (matchesQuery && matchesStatus) {
                filteredList.add(order);
            }
        }
        adapter.updateList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshOrderData();
        applyFilters();
    }
}