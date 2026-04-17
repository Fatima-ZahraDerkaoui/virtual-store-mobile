package com.virtualstore.virtualstore.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.activities.auth.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    public void setContentView(int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_admin, null);
        FrameLayout activityContainer = drawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawerLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!(this instanceof AdminDashboardActivity) && !(this instanceof AddEditProductActivity)) {
            getMenuInflater().inflate(R.menu.menu_search, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();

            if (searchView != null) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        onSearchPerformed(query != null ? query : "");
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        onSearchPerformed(newText != null ? newText : "");
                        return true;
                    }
                });
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void onSearchPerformed(String query) {
        // To be overridden by activities
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            onFilterClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFilterClicked() {
        // To be overridden by activities
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.menu_dashboard) {
            if (!(this instanceof AdminDashboardActivity)) {
                startActivity(new Intent(this, AdminDashboardActivity.class));
            }
        } else if (id == R.id.menu_products) {
            if (!(this instanceof ProductListAdminActivity)) {
                startActivity(new Intent(this, ProductListAdminActivity.class));
            }
        } else if (id == R.id.menu_categories) {
            if (!(this instanceof CategoryListActivity)) {
                startActivity(new Intent(this, CategoryListActivity.class));
            }
        } else if (id == R.id.menu_users) {
            if (!(this instanceof UserListActivity)) {
                startActivity(new Intent(this, UserListActivity.class));
            }
        } else if (id == R.id.menu_orders) {
            if (!(this instanceof OrderListActivity)) {
                startActivity(new Intent(this, OrderListActivity.class));
            }
        } else if (id == R.id.menu_stock) {
            if (!(this instanceof StockManagementActivity)) {
                startActivity(new Intent(this, StockManagementActivity.class));
            }
        } else if (id == R.id.menu_logout) {
            performLogout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void performLogout() {
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}