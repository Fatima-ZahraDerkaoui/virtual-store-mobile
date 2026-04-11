package com.virtualstore.virtualstore.Panier_Historique;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.OrderLineAdapter;
import com.virtualstore.virtualstore.database.OrderDAO;
import com.virtualstore.virtualstore.model.CartItem;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Toolbar avec flèche retour
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Détail Commande");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recycler = findViewById(R.id.recyclerOrderDetails);

        int orderId = getIntent().getIntExtra("order_id", -1);

        OrderDAO db = new OrderDAO(this);
        List<CartItem> list = db.getOrderLines(orderId);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new OrderLineAdapter(list));
    }

    // bouton retour
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}