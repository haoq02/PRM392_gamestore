package com.example.shopgame.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopgame.Models.UserDao;
import com.example.shopgame.Models.User;
import com.example.shopgame.Utils.AppDatabase;
import com.example.shopgame.R;

import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {

    private EditText fullNameEditText, phoneEditText, addressEditText;
    private Button saveButton;
    private SharedPreferences sharedPreferences;
    private UserDao userDao;
    private int userId; // To store the current userId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        fullNameEditText = findViewById(R.id.fullNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        saveButton = findViewById(R.id.saveButton);

        // Access shared preferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userDao = AppDatabase.getDatabase(this).userDao();
        userId = sharedPreferences.getInt("id", -1); // Get userId from SharedPreferences

        // Load current user information into EditTexts
        loadUserInfo();

        // Handle save button click
        saveButton.setOnClickListener(v -> saveUserInfo());
    }

    private void loadUserInfo() {
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId);
            if (user != null) {
                runOnUiThread(() -> {
                    fullNameEditText.setText(user.getFullName());
                    phoneEditText.setText(user.getPhoneNumber());
                    addressEditText.setText(user.getAddress());

                    // Save to SharedPreferences as well
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fullName", user.getFullName());
                    editor.putString("phone", user.getPhoneNumber());
                    editor.putString("address", user.getAddress());
                    editor.apply();
                });
            }
        });
    }

    private void saveUserInfo() {
        String newFullName = fullNameEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();
        String newAddress = addressEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newFullName) || TextUtils.isEmpty(newPhone) || TextUtils.isEmpty(newAddress)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save updated user info to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", newFullName);
        editor.putString("phone", newPhone);
        editor.putString("address", newAddress);
        editor.apply();

        // Save updated user info to Room Database
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId);
            if (user != null) {
                user.setFullName(newFullName);
                user.setPhoneNumber(newPhone);
                user.setAddress(newAddress);
                userDao.update(user);
            }
        });

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // Return to ProfileActivity
        setResult(RESULT_OK);
        finish();
    }
}
