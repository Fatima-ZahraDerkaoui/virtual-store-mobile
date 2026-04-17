package com.virtualstore.virtualstore.activities.panier_historique;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.database.OrderDAO;
import com.virtualstore.virtualstore.model.CartItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    TextView txtPaymentTotal;
    EditText edtCardName, edtCardNumber, edtCardExpiry, edtCardCVV;
    Button btnConfirm;

    double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Paiement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtPaymentTotal = findViewById(R.id.txtPaymentTotal);
        edtCardName = findViewById(R.id.edtCardName);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtCardExpiry = findViewById(R.id.edtCardExpiry);
        edtCardCVV = findViewById(R.id.edtCardCVV);
        btnConfirm = findViewById(R.id.btnConfirm);

        CartDAO cartDAO = new CartDAO(this);

        List<CartItem> cartItems = cartDAO.getAllItems();

        total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }

        txtPaymentTotal.setText("Total: " + total + " DH");

        btnConfirm.setOnClickListener(v -> {

            String name = edtCardName.getText().toString().trim();
            String number = edtCardNumber.getText().toString().trim();
            String expiry = edtCardExpiry.getText().toString().trim();
            String cvv = edtCardCVV.getText().toString().trim();

            if (name.isEmpty() || number.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (number.length() != 16) {
                Toast.makeText(this, "Numéro de carte invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            OrderDAO orderDAO = new OrderDAO(this);

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date());

            int orderId = orderDAO.addOrder(date, total);

            for (CartItem item : cartItems) {
                orderDAO.addOrderLine(orderId, item);
            }

            cartDAO.clearCart();

            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}