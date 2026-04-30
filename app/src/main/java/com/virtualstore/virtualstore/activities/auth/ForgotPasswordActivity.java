package com.virtualstore.virtualstore.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.UserDAO;

import java.security.MessageDigest;

public class ForgotPasswordActivity extends AppCompatActivity {

    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userDAO = new UserDAO(this);

        EditText email = findViewById(R.id.emailInput);
        EditText second = findViewById(R.id.secondNameInput);
        EditText color = findViewById(R.id.favoriteColorInput);
        EditText newPass = findViewById(R.id.newPasswordInput);
        EditText confirm = findViewById(R.id.confirmPasswordInput);

        LinearLayout resetLayout = findViewById(R.id.resetLayout);
        TextView backToLogin = findViewById(R.id.backToLogin);

        Button verify = findViewById(R.id.verifyButton);
        Button reset = findViewById(R.id.resetButton);

        resetLayout.setVisibility(View.GONE);

        verify.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String secondStr = second.getText().toString().trim();
            String colorStr = color.getText().toString().trim();

            if (emailStr.isEmpty() || secondStr.isEmpty() || colorStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userDAO.checkUserForReset(emailStr, secondStr, colorStr)) {
                resetLayout.setVisibility(View.VISIBLE);
                verify.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Informations incorrectes", Toast.LENGTH_SHORT).show();
            }
        });

        reset.setOnClickListener(v -> {
            String pass = newPass.getText().toString();
            String conf = confirm.getText().toString();

            if (pass.isEmpty() || pass.length() < 4) {
                Toast.makeText(this, "Mot de passe trop court", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(conf)) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashed = hash(pass);
            boolean ok = userDAO.updatePassword(email.getText().toString().trim(), hashed);

            if (ok) {
                Toast.makeText(this, "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });

        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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