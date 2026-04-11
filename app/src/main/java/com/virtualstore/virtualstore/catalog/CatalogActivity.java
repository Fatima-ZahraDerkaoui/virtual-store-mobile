package com.virtualstore.virtualstore.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.Panier_Historique.CartActivity;
import com.virtualstore.virtualstore.Panier_Historique.HistoryActivity;
import com.virtualstore.virtualstore.auth.LoginActivity;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.database.UserDAO;
import com.virtualstore.virtualstore.fragment.CatalogueFragment;
import com.virtualstore.virtualstore.model.User;

public class CatalogActivity extends AppCompatActivity {

    private TextView tvToolbarCartCount;
    private CartDAO cartDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        cartDAO = new CartDAO(this);
        tvToolbarCartCount = findViewById(R.id.tvToolbarCartCount);

        ImageButton btnHistory = findViewById(R.id.btnHistory);
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        ImageButton btnLogout = findViewById(R.id.btnLogout);
        ImageButton btnToolbarCart = findViewById(R.id.btnToolbarCart);

        btnHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });

        btnProfile.setOnClickListener(v -> {
            // Get user email from intent (passed from LoginActivity)
            String email = getIntent().getStringExtra("USER_EMAIL");
            if (email == null) email = "admin@store.com"; // Fallback for testing

            UserDAO userDAO = new UserDAO(this);
            User user = userDAO.getUserByEmail(email);

            if (user != null) {
                String info = "Name: " + user.getFirstName() + " " + user.getLastName() +
                             "\nEmail: " + user.getEmail() +
                             "\nColor: " + user.getFavoriteColor();
                Toast.makeText(this, info, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnToolbarCart.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        updateCartCount();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new CatalogueFragment())
                    .commit();
        }
    }

    public void updateCartCount() {
        if (tvToolbarCartCount != null) {
            int count = cartDAO.getAllItems().size();
            tvToolbarCartCount.setText(String.valueOf(count));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}