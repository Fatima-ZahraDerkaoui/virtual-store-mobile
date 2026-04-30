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
            getSupportActionBar().setTitle("Gérer les utilisateurs");
        }

        recyclerView = findViewById(R.id.recyclerUsers);

        // Ensure DataManager is initialized and data is loaded
        DataManager.init(this);
        DataManager.loadAllData();

        adapter = new UserAdapter(
            new ArrayList<>(DataManager.users),
            user -> showEditUserDialog(user),
            user -> {
                new AlertDialog.Builder(this)
                    .setTitle("Supprimer l'utilisateur")
                    .setMessage("Voulez-vous vraiment supprimer cet utilisateur ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        DataManager.deleteUser(user.getId());
                        applyFilters();
                    })
                    .setNegativeButton("Non", null)
                    .show();
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
        DataManager.loadAllData();
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

        EditText etFirstName = dialogView.findViewById(R.id.etEditUserFirstName);
        EditText etLastName = dialogView.findViewById(R.id.etEditUserLastName);
        EditText etEmail = dialogView.findViewById(R.id.etEditUserEmail);
        EditText etPhone = dialogView.findViewById(R.id.etEditUserPhone);
        EditText etAddress = dialogView.findViewById(R.id.etEditUserAddress);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerUserRole);

        if (etFirstName != null) etFirstName.setText(user.getFirstName());
        if (etLastName != null) etLastName.setText(user.getLastName());
        if (etEmail != null) etEmail.setText(user.getEmail());
        if (etPhone != null) etPhone.setText(user.getPhone());
        if (etAddress != null) etAddress.setText(user.getAddress());

        if (spinnerRole != null) {
            String[] roles = {"Client", "Admin"};
            ArrayAdapter<String> adapterRole = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
            adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRole.setAdapter(adapterRole);
            spinnerRole.setSelection(user.getRole() == 1 ? 1 : 0);
        }

        builder.setView(dialogView);
        builder.setTitle("Modifier l'utilisateur");
        builder.setPositiveButton("Enregistrer", (dialog, which) -> {
            if (etFirstName != null) user.setFirstName(etFirstName.getText().toString().trim());
            if (etLastName != null) user.setLastName(etLastName.getText().toString().trim());
            if (etEmail != null) user.setEmail(etEmail.getText().toString().trim());
            if (etPhone != null) user.setPhone(etPhone.getText().toString().trim());
            if (etAddress != null) user.setAddress(etAddress.getText().toString().trim());
            if (spinnerRole != null) user.setRole(spinnerRole.getSelectedItemPosition());

            DataManager.updateUser(user);
            applyFilters();
            Toast.makeText(this, "Utilisateur mis à jour", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyFilters();
    }
}
