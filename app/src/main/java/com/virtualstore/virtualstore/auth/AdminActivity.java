package com.virtualstore.virtualstore.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.virtualstore.virtualstore.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView adminWelcome = findViewById(R.id.adminWelcome);
        Button manageProfileBtn = findViewById(R.id.manageProfileBtn);
        Button logoutBtn = findViewById(R.id.logoutBtn);

        String adminEmail = getIntent().getStringExtra("ADMIN_EMAIL");
        adminWelcome.setText("Welcome, " + adminEmail);

        manageProfileBtn.setOnClickListener(v -> {
            // Here you could open a profile editor activity
            Toast.makeText(this, "Redirection vers modification profil...", Toast.LENGTH_SHORT).show();
        });

        logoutBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
