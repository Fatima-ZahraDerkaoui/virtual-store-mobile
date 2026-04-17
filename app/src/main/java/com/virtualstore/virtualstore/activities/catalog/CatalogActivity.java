package com.virtualstore.virtualstore.activities.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.activities.panier_historique.CartActivity;
import com.virtualstore.virtualstore.activities.panier_historique.HistoryActivity;
import com.virtualstore.virtualstore.activities.auth.LoginActivity;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.database.UserDAO;
import com.virtualstore.virtualstore.activities.fragment.CatalogueFragment;
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

        if (btnHistory != null) {
            btnHistory.setOnClickListener(v -> {
                startActivity(new Intent(this, HistoryActivity.class));
            });
        }

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                String email = getIntent().getStringExtra("USER_EMAIL");
                if (email == null) {
                    Toast.makeText(this, "Utilisateur non identifié", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDAO userDAO = new UserDAO(this);
                User user = userDAO.getUserByEmail(email);

                if (user != null) {
                    String info = "Nom: " + user.getFirstName() + " " + user.getLastName() +
                                 "\nEmail: " + user.getEmail() +
                                 "\nCouleur: " + user.getFavoriteColor();
                    Toast.makeText(this, info, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Profil introuvable", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnToolbarCart != null) {
            btnToolbarCart.setOnClickListener(v -> {
                startActivity(new Intent(this, CartActivity.class));
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });
        }

        updateCartCount();

        // Safety check for fragment container
        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new CatalogueFragment())
                        .commit();
            }
        } else {
            Toast.makeText(this, "Erreur d'affichage : Conteneur introuvable", Toast.LENGTH_LONG).show();
        }
    }

    public void updateCartCount() {
        if (tvToolbarCartCount != null) {
            try {
                int count = cartDAO.getAllItems().size();
                tvToolbarCartCount.setText(String.valueOf(count));
            } catch (Exception e) {
                tvToolbarCartCount.setText("0");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}