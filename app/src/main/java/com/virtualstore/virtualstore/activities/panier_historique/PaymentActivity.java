package com.virtualstore.virtualstore.activities.panier_historique;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.database.OrderDAO;
import com.virtualstore.virtualstore.model.CartItem;

import java.text.SimpleDateFormat;
import java.util.*;

public class PaymentActivity extends AppCompatActivity {

    TextView txtTotal;
    List<CartItem> list;
    double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Paiement");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtTotal = findViewById(R.id.txtPaymentTotal);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        CartDAO cart = new CartDAO(this);
        OrderDAO oder = new OrderDAO(this);
        list = cart.getAllItems();

        // ✅ FIX TOTAL
        for (CartItem item : list) {
            total += item.getPrice() * item.getQuantity();
        }

        txtTotal.setText("Total: " + total + " DH");

        btnConfirm.setOnClickListener(v -> {

            if (list.isEmpty()) {
                Toast.makeText(this, "Panier vide", Toast.LENGTH_SHORT).show();
                return;
            }

            // convertir panier en texte
            StringBuilder details = new StringBuilder();
            for (CartItem item : list) {
                details.append(item.getName())
                        .append(" x")
                        .append(item.getQuantity())
                        .append("\n");
            }

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                    Locale.getDefault()).format(new Date());

            // ajouter commande
            int orderId = oder.addOrder(date, total);
            // ajouter chaque produit
            for (CartItem item : list) {
                oder.addOrderLine(orderId, item);
            }

            // vider panier
            cart.clearCart();

            Toast.makeText(this, "Commande confirmée", Toast.LENGTH_LONG).show();

            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
