package com.virtualstore.virtualstore.activities.panier_historique;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.CartAdapter;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    TextView txtTotal;
    RecyclerView recyclerView;
    Button btnCheckout;

    List<CartItem> list = new ArrayList<>();
    CartDAO db;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Panier");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        db = new CartDAO(this);

        list.addAll(db.getAllItems());

        adapter = new CartAdapter(list, db, this::calculateTotalValue);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        calculateTotalValue();

        btnCheckout.setOnClickListener(v -> {

            double total = calculateTotalValue();

            Intent i = new Intent(this, PaymentActivity.class);
            i.putExtra("total", total);

            startActivityForResult(i, 1001);
        });
    }

    private double calculateTotalValue() {

        double total = 0;

        for (CartItem item : list) {
            total += item.getPrice() * item.getQuantity();
        }

        txtTotal.setText("Total: " + total + " DH");

        return total;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {

            db.clearCart();
            list.clear();
            adapter.notifyDataSetChanged();
            calculateTotalValue();

            Toast.makeText(this, "Paiement réussi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        list.clear();
        list.addAll(db.getAllItems());
        adapter.notifyDataSetChanged();
        calculateTotalValue();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}