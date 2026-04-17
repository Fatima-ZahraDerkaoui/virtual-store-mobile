package com.virtualstore.virtualstore.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.activities.admin.AdminDashboardActivity;
import com.virtualstore.virtualstore.activities.catalog.CatalogActivity;
import com.virtualstore.virtualstore.database.UserDAO;

import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDAO = new UserDAO(this);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLink = findViewById(R.id.registerLink);
        TextView forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        // Login Logic
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            String hashed = hash(password);

            if (userDAO.checkUser(email, hashed)) {
                int isAdmin = userDAO.getIsAdmin(email);
                
                if (isAdmin == 1) {
                    // Redirect to the New Admin Dashboard
                    Intent i = new Intent(this, AdminDashboardActivity.class);
                    i.putExtra("ADMIN_EMAIL", email);
                    startActivity(i);
                } else {
                    // Redirect to Catalog for normal users
                    String name = userDAO.getFirstNameByEmail(email);
                    Intent i = new Intent(this, CatalogActivity.class);
                    i.putExtra("USER_FIRST_NAME", name);
                    i.putExtra("USER_EMAIL", email);
                    startActivity(i);
                }
                finish();
            } else {
                Toast.makeText(this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to Register
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Navigate to Forgot Password
        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}