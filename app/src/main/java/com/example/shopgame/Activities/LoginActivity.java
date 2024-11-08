    package com.example.shopgame.Activities;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.shopgame.Models.User;
    import com.example.shopgame.Models.UserDao;
    import com.example.shopgame.Utils.AppDatabase;
    import com.example.shopgame.R;

    import java.util.concurrent.Executors;

    public class LoginActivity extends AppCompatActivity {

        private static final String TAG = "LoginActivity";
        private EditText usernameEditText, passwordEditText;
        private Button loginButton, signupButton;
        private UserDao userDao;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            Log.d(TAG, "onCreate: LoginActivity started");

            usernameEditText = findViewById(R.id.usernameEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            loginButton = findViewById(R.id.loginButton);
            signupButton = findViewById(R.id.signupButton);

            userDao = AppDatabase.getDatabase(this).userDao();

            // Thêm dữ liệu mẫu người dùng
            addSampleUser();

            loginButton.setOnClickListener(v -> {
                Log.d(TAG, "Login button clicked");
                loginUser();
            });

            signupButton.setOnClickListener(v -> {
                Log.d(TAG, "SignUp button clicked");
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            });
        }

        // Thêm phương thức để thêm người dùng mẫu
        private void addSampleUser() {
            Executors.newSingleThreadExecutor().execute(() -> {
                User existingUser = userDao.getUserByEmail("hao@gmail.com");
                if (existingUser == null) {
                    // Tạo user mẫu với role "user"
                    User sampleUser = new User(
                            "1", // Password
                            "Hao Nguyen", // Full name
                            "hao@gmail.com", // Email
                            "123-456-7890", // Phone number
                            "123 Sample Street, Sample City", // Address
                            "user" // Role
                    );
                    userDao.insert(sampleUser);
                    Log.d(TAG, "Sample user added: hao@gmail.com with password 1");
                } else {
                    Log.d(TAG, "Sample user already exists");
                }

                User existingAdmin = userDao.getUserByEmail("admin@example.com");
                if (existingAdmin == null) {
                    // Tạo user mẫu với role "admin"
                    User adminUser = new User(
                            "1", // Password
                            "Admin User", // Full name
                            "admin@example.com", // Email
                            "987-654-3210", // Phone number
                            "Admin Street, Admin City", // Address
                            "admin" // Role
                    );
                    userDao.insert(adminUser);
                    Log.d(TAG, "Admin user added: admin@example.com with password 1");
                } else {
                    Log.d(TAG, "Admin user already exists");
                }
            });
        }





        private void loginUser() {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Empty email or password");
                return;
            }

            Log.d(TAG, "Attempting to log in with email: " + email);

            Executors.newSingleThreadExecutor().execute(() -> {
                User user = userDao.getUserByEmail(email);
                if (user != null && user.getPassword().equals(password)) {
                    Log.d(TAG, "Login successful for email: " + email);

                    // Save user info into SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", user.getId());
                    editor.putString("email", user.getEmail());
                    editor.putString("fullName", user.getFullName());
                    editor.putString("phone", user.getPhoneNumber());
                    editor.putString("address", user.getAddress());
                    editor.putString("role", user.getRole()); // Save the role
                    editor.apply();

                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Navigating to appropriate activity");

                        // Navigate based on role
                        if ("admin".equals(user.getRole())) {
                            Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
                            startActivity(intent);
                        }
                        finish(); // Close LoginActivity
                    });
                } else {
                    Log.d(TAG, "Login failed for email: " + email);
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show());
                }
            });
        }

    }
