package com.example.shopgame.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopgame.Models.User;
import com.example.shopgame.Models.UserDao;
import com.example.shopgame.R;
import com.example.shopgame.Utils.AppDatabase;

import java.util.concurrent.Executors;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button updatePasswordButton;
    private UserDao userDao;
    private SharedPreferences sharedPreferences;
    private int userId; // ID của người dùng hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        userDao = AppDatabase.getDatabase(this).userDao();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Lấy userId từ SharedPreferences
        userId = sharedPreferences.getInt("id", -1);

        updatePasswordButton.setOnClickListener(v -> updatePassword());
    }

    private void updatePassword() {
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra và cập nhật mật khẩu
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserById(userId); // Lấy thông tin người dùng hiện tại
            if (user != null && user.getPassword().equals(currentPassword)) {
                // Cập nhật mật khẩu mới
                user.setPassword(newPassword);
                userDao.update(user);

                runOnUiThread(() -> {
                    Toast.makeText(ChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    // Xóa các trường nhập
                    currentPasswordEditText.setText("");
                    newPasswordEditText.setText("");
                    confirmPasswordEditText.setText("");
                });
            } else {
                runOnUiThread(() -> Toast.makeText(ChangePasswordActivity.this, "Current password is incorrect", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
