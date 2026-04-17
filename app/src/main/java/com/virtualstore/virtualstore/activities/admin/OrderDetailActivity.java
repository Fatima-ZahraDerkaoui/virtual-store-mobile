package com.virtualstore.virtualstore.activities.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.DataManager;
import com.virtualstore.virtualstore.model.Order;
import com.virtualstore.virtualstore.model.User;

public class OrderDetailActivity extends BaseAdminActivity {

    private TextView tvOrderId, tvOrderDate, tvOrderStatus, tvUserName, tvUserEmail, tvUserPhone, tvUserAddress, tvOrderTotal;
    private Button btnCall, btnMap, btnUpdateStatus;
    private Order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Order Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();

        String orderId = getIntent().getStringExtra("ORDER_ID");
        for (Order o : DataManager.orders) {
            if (o.getId().equals(orderId)) {
                order = o;
                break;
            }
        }

        if (order != null) {
            populateData(order);
        } else {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvOrderId = findViewById(R.id.tvDetailOrderId);
        tvOrderDate = findViewById(R.id.tvDetailOrderDate);
        tvOrderStatus = findViewById(R.id.tvDetailOrderStatus);
        tvUserName = findViewById(R.id.tvDetailUserName);
        tvUserEmail = findViewById(R.id.tvDetailUserEmail);
        tvUserPhone = findViewById(R.id.tvDetailUserPhone);
        tvUserAddress = findViewById(R.id.tvDetailUserAddress);
        tvOrderTotal = findViewById(R.id.tvDetailOrderTotal);

        btnCall = findViewById(R.id.btnCallCustomer);
        btnMap = findViewById(R.id.btnShowMap);
        btnUpdateStatus = findViewById(R.id.btnUpdateOrderStatus);

        btnCall.setOnClickListener(v -> {
            User user = null;
            if (order != null) {
                for (User u : DataManager.users) {
                    if (u.getId().equals(order.getUserId())) {
                        user = u;
                        break;
                    }
                }
            }
            if (user != null && user.getPhone() != null && !user.getPhone().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + user.getPhone()));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Phone not available", Toast.LENGTH_SHORT).show();
            }
        });

        btnMap.setOnClickListener(v -> {
            User user = null;
            if (order != null) {
                for (User u : DataManager.users) {
                    if (u.getId().equals(order.getUserId())) {
                        user = u;
                        break;
                    }
                }
            }
            if (user != null && user.getAddress() != null && !user.getAddress().isEmpty()) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(user.getAddress()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Address not available", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdateStatus.setOnClickListener(v -> {
            if (order != null) showUpdateStatusDialog(order);
        });
    }

    private void populateData(Order order) {
        User user = null;
        for (User u : DataManager.users) {
            if (u.getId().equals(order.getUserId())) {
                user = u;
                break;
            }
        }

        tvOrderId.setText("Order #" + order.getId());
        tvOrderDate.setText("Date: " + order.getDate());
        tvOrderStatus.setText("Status: " + order.getStatus());

        if (user != null) {
            tvUserName.setText(user.getFirstName() + " " + user.getLastName());
            tvUserEmail.setText(user.getEmail());
            tvUserPhone.setText(user.getPhone());
            tvUserAddress.setText(user.getAddress());
        }

        tvOrderTotal.setText("Total: " + order.getTotalPrice() + " DH");
    }

    private void showUpdateStatusDialog(Order order) {
        String[] statusOptions = {"Pending", "Shipped", "Delivered", "Cancelled"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status");
        builder.setItems(statusOptions, (dialog, which) -> {
            String newStatus = statusOptions[which];
            order.setStatus(newStatus);
            DataManager.updateOrder(order);
            tvOrderStatus.setText("Status: " + newStatus);
            Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
