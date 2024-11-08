package com.example.shopgame.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopgame.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView, emailTextView, phoneTextView, addressTextView;
    private Button changePasswordButton, viewOrderHistoryButton, viewFavoritesButton, logoutButton, editProfileButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        fullNameTextView = findViewById(R.id.fullNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        addressTextView = findViewById(R.id.addressTextView);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        viewOrderHistoryButton = findViewById(R.id.viewOrderHistoryButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);
        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        // Access shared preferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Display user information
        updateProfileInfo();

        // Navigate to ChangePasswordActivity
        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Navigate to OrderHistoryActivity
        viewOrderHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Navigate to FavoritesActivity
//        viewFavoritesButton.setOnClickListener(v -> {
//            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
//            startActivity(intent);
//        });

        // Navigate to EditProfileActivity
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, 1); // Use startActivityForResult
        });

        // Handle logout
        logoutButton.setOnClickListener(v -> logout());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Update profile info when returning from EditProfileActivity
            updateProfileInfo();
        }
    }

    private void updateProfileInfo() {
        String fullName = sharedPreferences.getString("fullName", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String phone = sharedPreferences.getString("phone", "N/A");
        String address = sharedPreferences.getString("address", "N/A");

        fullNameTextView.setText(fullName);
        emailTextView.setText(email);
        phoneTextView.setText(phone);
        addressTextView.setText(address);
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
