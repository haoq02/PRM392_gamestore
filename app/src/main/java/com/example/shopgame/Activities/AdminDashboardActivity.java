package com.example.shopgame.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopgame.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button addProductButton, viewUsersButton, manageProductsButton, logoutButton; // New button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize buttons
        addProductButton = findViewById(R.id.addProductButton);
        viewUsersButton = findViewById(R.id.viewUsersButton);
        manageProductsButton = findViewById(R.id.manageProductsButton);
        logoutButton = findViewById(R.id.logoutButton); // Initialize new button

        // Handle Add Product button click
        addProductButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        // Handle View Users button click
        viewUsersButton.setOnClickListener(v -> {
            Toast.makeText(AdminDashboardActivity.this, "View Users clicked", Toast.LENGTH_SHORT).show();
        });

        // Handle Manage Products button click
        manageProductsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageProductsActivity.class);
            startActivity(intent);
        });

        // Handle Logout button click
        logoutButton.setOnClickListener(v -> {
            // Clear user session data (e.g., using SharedPreferences)
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Navigate to LoginActivity
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Close current activity
        });
    }
}
