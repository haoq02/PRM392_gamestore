package com.example.shopgame.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopgame.Models.CartDao;
import com.example.shopgame.Models.CartItem;
import com.example.shopgame.Models.Order;
import com.example.shopgame.Models.OrderDao;
import com.example.shopgame.Models.UserDao;
import com.example.shopgame.Models.User;
import com.example.shopgame.R;
import com.example.shopgame.Utils.AppDatabase;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CheckoutActivity extends AppCompatActivity {

    private TextView totalAmountTextView;
    private EditText fullNameEditText, phoneEditText, addressEditText;
    private CartDao cartDao;
    private OrderDao orderDao;
    private UserDao userDao;
    private double totalAmount = 0;
    private Button confirmCheckoutButton;
    private SharedPreferences sharedPreferences;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        confirmCheckoutButton = findViewById(R.id.confirmCheckoutButton);

        cartDao = AppDatabase.getDatabase(this).cartDao();
        orderDao = AppDatabase.getDatabase(this).orderDao();
        userDao = AppDatabase.getDatabase(this).userDao();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "N/A");

        loadUserData();
        calculateTotalAmount();

        confirmCheckoutButton.setOnClickListener(v -> performCheckout());
    }

    private void loadUserData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDao.getUserByEmail(userEmail);
            if (user != null) {
                runOnUiThread(() -> {
                    fullNameEditText.setText(user.getFullName());
                    phoneEditText.setText(user.getPhoneNumber());
                    addressEditText.setText(user.getAddress());
                });
            }
        });
    }

    private void calculateTotalAmount() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<CartItem> cartItems = cartDao.getAllCartItems();
            totalAmount = 0;

            for (CartItem item : cartItems) {
                totalAmount += item.getPrice() * item.getQuantity();
            }

            runOnUiThread(() -> totalAmountTextView.setText("Total: $" + String.format("%.2f", totalAmount)));
        });
    }

    private void performCheckout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1); // Lấy userId từ SharedPreferences

        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            List<CartItem> cartItems = cartDao.getCartItemsByUserId(userId); // Lọc theo userId
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            for (CartItem item : cartItems) {
                Order order = new Order(
                        item.getName(),
                        item.getQuantity(),
                        item.getPrice() * item.getQuantity(),
                        currentDate,
                        item.getImageResId(),
                        userId // Thêm userId vào Order
                );
                orderDao.insert(order);
            }

            cartDao.clearCartByUserId(userId); // Xóa giỏ hàng của người dùng cụ thể

            runOnUiThread(() -> {
                Toast.makeText(CheckoutActivity.this, "Thank you for your purchase!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckoutActivity.this, ProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        });
    }
}
