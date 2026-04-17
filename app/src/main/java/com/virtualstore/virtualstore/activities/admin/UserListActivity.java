package com.virtualstore.virtualstore.activities.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.adapter.UserAdapter;
import com.virtualstore.virtualstore.database.DataManager;
import com.virtualstore.virtualstore.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends BaseAdminActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Users");
        }

        recyclerView = findViewById(R.id.recyclerUsers);

        adapter = new UserAdapter(
            DataManager.users,
            user -> showEditUserDialog(user),
            user -> {
                DataManager.deleteUser(user.getId());
                applyFilters();
            }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSearchPerformed(String query) {
        currentQuery = query;
        applyFilters();
    }

    private void applyFilters() {
        List<User> filteredList = new ArrayList<>();
        for (User user : DataManager.users) {
            boolean matches = (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(currentQuery.toLowerCase())) ||
                              (user.getLastName() != null && user.getLastName().toLowerCase().contains(currentQuery.toLowerCase())) ||
                              (user.getEmail() != null && user.getEmail().toLowerCase().contains(currentQuery.toLowerCase()));
            if (matches) {
                filteredList.add(user);
            }
        }
        adapter.updateList(filteredList);
    }

    private void showEditUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_user, null);

        EditText etName = dialogView.findViewById(R.id.etEditUserName);
        EditText etEmail = dialogView.findViewById(R.id.etEditUserEmail);
        EditText etPhone = dialogView.findViewById(R.id.etEditUserPhone);
        EditText etAddress = dialogView.findViewById(R.id.etEditUserAddress);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerUserRole);

        // Setup Spinner for Role (0: Client, 1: Admin)
        String[] roles = {"Client", "Admin"};
        ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapterRole);

        // Pre-select current role
        spinnerRole.setSelection(user.getRole() == 1 ? 1 : 0);

        etName.setText(user.getFirstName() + " " + user.getLastName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());

        builder.setView(dialogView);
        builder.setTitle("Edit User");
        builder.setPositiveButton("Save", (dialog, which) -> {
            String fullName = etName.getText().toString().trim();
            String[] parts = fullName.split(" ", 2);
            user.setFirstName(parts.length > 0 ? parts[0] : "");
            user.setLastName(parts.length > 1 ? parts[1] : "");

            user.setEmail(etEmail.getText().toString());
            user.setPhone(etPhone.getText().toString());
            user.setAddress(etAddress.getText().toString());

            // Update role from spinner selection
            user.setRole(spinnerRole.getSelectedItemPosition());

            DataManager.updateUser(user);
            applyFilters();
            Toast.makeText(this, "User Updated", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
