package com.virtualstore.virtualstore.activities.panier_historique;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.CartAdapter;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.model.CartItem;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    TextView txtTotal;
    List<CartItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Panier");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recycler = findViewById(R.id.recyclerCart);
        txtTotal = findViewById(R.id.txtTotal);
        Button btnCheckout = findViewById(R.id.btnCheckout);

        CartDAO db = new CartDAO(this);
        list = db.getAllItems();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        CartDAO cartDAO = new CartDAO(this);

        CartAdapter adapter = new CartAdapter(
                list,
                cartDAO,
                this::calculateTotal
        );

        recycler.setAdapter(adapter);

        calculateTotal();

        btnCheckout.setOnClickListener(v -> {
            Intent i = new Intent(this, PaymentActivity.class);
            startActivity(i);
        });
    }

    private void calculateTotal() {
        double total = 0;
        for (CartItem item : list) {
            total += item.getPrice() * item.getQuantity();
        }
        txtTotal.setText("Total: " + total + " DH");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
