package com.example.shopgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopgame.Adapters.CartAdapter;
import com.example.shopgame.Models.CartDao;
import com.example.shopgame.Models.CartItem;
import com.example.shopgame.Utils.AppDatabase;
import com.example.shopgame.R;

import java.util.List;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private CartAdapter cartAdapter;
    private CartDao cartDao;
    private int userId; // Thêm userId để lọc giỏ hàng theo người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartDao = AppDatabase.getDatabase(this).cartDao();

        // Lấy userId từ SharedPreferences
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("id", -1);
        if (userId == -1) {
            // Nếu không có userId, có thể xử lý hoặc thông báo cho người dùng
            totalPriceTextView.setText("No user logged in");
            return;
        }

        loadCartItems();

        // Sự kiện bấm nút Checkout
        checkoutButton.setOnClickListener(v -> {
            double totalAmount = calculateTotalPrice(); // Lấy tổng giá
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("totalAmount", totalAmount); // Truyền tổng giá sang CheckoutActivity
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Lọc giỏ hàng theo userId
            List<CartItem> cartItems = cartDao.getCartItemsByUserId(userId);
            runOnUiThread(() -> {
                cartAdapter = new CartAdapter(this, cartItems, this::calculateTotalPrice);
                cartRecyclerView.setAdapter(cartAdapter);
                calculateTotalPrice(); // Gọi hàm tính tổng giá khi load xong cart items
            });
        });
    }

    // Tính tổng giá tiền
    private double calculateTotalPrice() {
        double totalPrice = 0;
        if (cartAdapter != null) {
            for (CartItem item : cartAdapter.getCartItems()) {
                totalPrice += item.getPrice() * item.getQuantity();
            }
        }
        totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice));
        return totalPrice; // Trả về tổng giá tiền
    }
}
