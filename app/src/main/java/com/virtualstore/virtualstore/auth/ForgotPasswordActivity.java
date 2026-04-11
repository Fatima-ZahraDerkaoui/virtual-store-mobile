package com.virtualstore.virtualstore.auth;

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

        Button verify = findViewById(R.id.verifyButton);
        Button reset = findViewById(R.id.resetButton);

        resetLayout.setVisibility(View.GONE);

        verify.setOnClickListener(v -> {

            if (userDAO.checkUserForReset(
                    email.getText().toString(),
                    second.getText().toString(),
                    color.getText().toString())) {

                resetLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Wrong info", Toast.LENGTH_SHORT).show();
            }
        });

        reset.setOnClickListener(v -> {

            String pass = newPass.getText().toString();
            String conf = confirm.getText().toString();

            if (!pass.equals(conf)) {
                Toast.makeText(this, "Not match", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashed = hash(pass);

            boolean ok = userDAO.updatePassword(
                    email.getText().toString(),
                    hashed
            );

            if (ok) {
                Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
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