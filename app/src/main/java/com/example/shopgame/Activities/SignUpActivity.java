    package com.example.shopgame.Activities;

    import android.os.Bundle;
    import android.text.TextUtils;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import com.example.shopgame.Models.User;
    import com.example.shopgame.Models.UserDao;
    import com.example.shopgame.Utils.AppDatabase;
    import com.example.shopgame.R;

    import java.util.concurrent.Executors;

    public class SignUpActivity extends AppCompatActivity {

        private EditText passwordEditText, confirmPasswordEditText;
        private EditText fullNameEditText, emailEditText, phoneNumberEditText, addressEditText;
        private Button signUpButton;
        private UserDao userDao;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

            // Initialize views
            fullNameEditText = findViewById(R.id.fullNameEditText);
            emailEditText = findViewById(R.id.emailEditText);
            phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
            addressEditText = findViewById(R.id.addressEditText);
            signUpButton = findViewById(R.id.signUpButton);

            userDao = AppDatabase.getDatabase(this).userDao();

            signUpButton.setOnClickListener(v -> signUpUser());
        }

        private void signUpUser() {
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim(); // Get address input

            if (!validateInput(fullName, email, phoneNumber, password, confirmPassword, address)) {
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                User existingUser = userDao.getUserByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Email already exists", Toast.LENGTH_SHORT).show());
                } else {
                    // Set role to "user" by default
                    String defaultRole = "user";
                    User newUser = new User(password, fullName, email, phoneNumber, address, defaultRole); // Include role
                    userDao.insert(newUser);
                    runOnUiThread(() -> {
                        Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        }

        private boolean validateInput(String fullName, String email, String phoneNumber, String password, String confirmPassword, String address) {
            // Check if password is empty or too short
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if password contains at least one digit and one special character
            if (!password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*]).{6,}$")) {
                Toast.makeText(this, "Password must contain at least one digit and one special character", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if confirm password matches the password
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if full name is empty
            if (TextUtils.isEmpty(fullName)) {
                Toast.makeText(this, "Full name cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if email is empty or invalid
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if phone number is empty
            if (TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if address is empty
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }
    }
