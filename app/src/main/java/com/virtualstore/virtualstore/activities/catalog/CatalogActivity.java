package com.virtualstore.virtualstore.activities.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.MaterialToolbar;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.activities.panier_historique.CartActivity;
import com.virtualstore.virtualstore.activities.panier_historique.HistoryActivity;
import com.virtualstore.virtualstore.activities.auth.LoginActivity;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.database.UserDAO;
import com.virtualstore.virtualstore.activities.fragment.CatalogueFragment;
import com.virtualstore.virtualstore.model.User;

public class CatalogActivity extends AppCompatActivity {

    private CartDAO cartDAO;
    private UserDAO userDAO;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        cartDAO = new CartDAO(this);
        userDAO = new UserDAO(this);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new CatalogueFragment());
                return true;
            } else if (id == R.id.nav_categories) {
                // Showing the catalogue which has categories filter at the top
                loadFragment(new CatalogueFragment());
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return false; 
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, HistoryActivity.class));
                return false;
            }
            return false;
        });

        if (savedInstanceState == null) {
            loadFragment(new CatalogueFragment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Rechercher un produit...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                    if (currentFragment instanceof CatalogueFragment) {
                        ((CatalogueFragment) currentFragment).filterBySearch(newText);
                    }
                    return true;
                }
            });
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            showUserInfo();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUserInfo() {
        if (userEmail == null) {
            Toast.makeText(this, "Session utilisateur non trouvée", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = userDAO.getUserByEmail(userEmail);
        if (user != null) {
            new AlertDialog.Builder(this)
                .setTitle("Mon Profil")
                .setMessage("Nom: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                         "Email: " + user.getEmail() + "\n" +
                         "Téléphone: " + (user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : "N/A"))
                .setPositiveButton("OK", null)
                .show();
        } else {
            Toast.makeText(this, "Informations non disponibles", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        new AlertDialog.Builder(this)
            .setTitle("Déconnexion")
            .setMessage("Voulez-vous vraiment vous déconnecter ?")
            .setPositiveButton("Oui", (dialog, which) -> {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Non", null)
            .show();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void updateCartCount() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        int count = cartDAO.getAllItems().size();
        if (count > 0) {
            bottomNav.getOrCreateBadge(R.id.nav_cart).setNumber(count);
        } else {
            bottomNav.removeBadge(R.id.nav_cart);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}