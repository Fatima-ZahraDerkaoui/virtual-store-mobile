package com.virtualstore.virtualstore.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.UserDAO;
import com.virtualstore.virtualstore.model.User;

import java.security.MessageDigest;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameInput, secondNameInput, lastNameInput,
            emailInput, passwordInput, confirmPasswordInput, favoriteColorInput;

    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDAO = new UserDAO(this);

        firstNameInput = findViewById(R.id.firstNameInput);
        secondNameInput = findViewById(R.id.secondNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        favoriteColorInput = findViewById(R.id.favoriteColorInput);
        
        TextView loginLink = findViewById(R.id.loginLink);
        Button registerButton = findViewById(R.id.registerButton);

        // Navigation to Login
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // REAL TIME VALIDATION
        emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateEmail();
        });

        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validatePassword();
        });

        confirmPasswordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateConfirmPassword();
        });

        registerButton.setOnClickListener(v -> registerUser());
    }

    // ================= VALIDATION =================

    private boolean validateEmail() {
        String email = emailInput.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Email invalide");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        String password = passwordInput.getText().toString().trim();
        if (password.length() < 4) {
            passwordInput.setError("Min 4 caractères");
            return false;
        }
        return true;
    }

    private boolean validateConfirmPassword() {
        String pass = passwordInput.getText().toString().trim();
        String confirm = confirmPasswordInput.getText().toString().trim();
        if (!pass.equals(confirm)) {
            confirmPasswordInput.setError("Les mots de passe ne correspondent pas");
            return false;
        }
        return true;
    }

    // ================= REGISTER =================

    private void registerUser() {
        String firstName = firstNameInput.getText().toString().trim();
        String secondName = secondNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String favoriteColor = favoriteColorInput.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || secondName.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validateEmail() || !validatePassword() || !validateConfirmPassword()) {
            return;
        }

        if (userDAO.checkEmail(email)) {
            emailInput.setError("Cet email existe déjà");
            return;
        }

        String hashedPassword = hash(password);
        User user = new User(firstName, secondName, lastName, email, hashedPassword, favoriteColor);

        if (userDAO.insertUser(user)) {
            Toast.makeText(this, "Compte créé !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de la création", Toast.LENGTH_SHORT).show();
        }
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