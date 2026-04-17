package com.virtualstore.virtualstore.activities.admin;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.virtualstore.virtualstore.R;

public class ClientMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Corrected layout reference to match your project
        setContentView(R.layout.activity_admin_dashboard);

        TextView welcomeText = findViewById(R.id.tvProductsCount);
        String name = getIntent().getStringExtra("USER_FIRST_NAME");
        if (name == null) name = "Client";
        
        if (welcomeText != null) {
            welcomeText.setText("Bienvenue, " + name + " !");
        }
    }
}
