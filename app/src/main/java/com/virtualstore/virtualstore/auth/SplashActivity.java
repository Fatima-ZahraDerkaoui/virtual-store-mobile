package com.virtualstore.virtualstore.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.UserDAO;
import com.virtualstore.virtualstore.model.User;

import java.security.MessageDigest;

public class SplashActivity extends AppCompatActivity {

    private TextView splashText;
    private String fullText = "Welcome to our virtual store";
    private int index = 0;
    private Handler handler = new Handler();
    private UserDAO userDAO;

    private final int delay = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userDAO = new UserDAO(this);
        splashText = findViewById(R.id.splashText);

        createAdminAccountIfMissing();
        startTypingAnimation();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 4000);
    }

    private void createAdminAccountIfMissing() {
        String adminEmail = "admin@store.com";
        if (!userDAO.checkEmail(adminEmail)) {
            // Create Admin User: FirstName, SecondName, LastName, Email, HashedPassword, Color, IsAdmin=1
            User admin = new User("Admin", "Main", "System", adminEmail, hash("admin"), "Blue", 1);
            userDAO.insertUser(admin);
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

    private void startTypingAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index < fullText.length()) {
                    splashText.setText(fullText.substring(0, index + 1));
                    index++;
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
    }
}
