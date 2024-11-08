package com.example.shopgame.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.shopgame.Models.CartItem;
import com.example.shopgame.Models.CartDao;
import com.example.shopgame.Utils.AppDatabase;
import com.example.shopgame.R;

import java.util.concurrent.Executors;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, quantityTextView;
    private ImageView productImageView;
    private Button addToCartButton, increaseQuantityButton, decreaseQuantityButton;
    private CartDao cartDao;
    private SharedPreferences sharedPreferences;
    private int quantity = 1; // Default quantity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize views
        productNameTextView = findViewById(R.id.productNameTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productImageView = findViewById(R.id.productImageView);
        addToCartButton = findViewById(R.id.addToCartButton);
        quantityTextView = findViewById(R.id.quantityTextView);
        increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Get data from Intent
        String productName = getIntent().getStringExtra("name");
        double productPrice = getIntent().getDoubleExtra("price", 0.0);
        String productDescription = getIntent().getStringExtra("description");
        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.default_image);

        // Display data in views
        productNameTextView.setText(productName);
        productPriceTextView.setText("$" + productPrice);
        productDescriptionTextView.setText(productDescription);
        productImageView.setImageResource(imageResId);

        // Initialize CartDao
        cartDao = AppDatabase.getDatabase(this).cartDao();

        // Set up quantity buttons
        increaseQuantityButton.setOnClickListener(v -> {
            quantity++;
            quantityTextView.setText(String.valueOf(quantity));
        });

        decreaseQuantityButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        // Add product to cart on button click
        addToCartButton.setOnClickListener(v -> addToCart(productName, productPrice, imageResId));
    }

    private void addToCart(String name, double price, int imageResId) {
        int userId = sharedPreferences.getInt("id", -1); // Get userId from SharedPreferences
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            // Query for the cart item for this user
            CartItem existingCartItem = cartDao.getCartItemByNameAndUserId(name, userId);
            if (existingCartItem != null) {
                // If item already in cart, increase quantity
                existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                cartDao.update(existingCartItem);
            } else {
                // If item not in cart, insert new item with userId
                CartItem newCartItem = new CartItem(name, price, quantity, imageResId, userId);
                cartDao.insert(newCartItem);
            }

            // Broadcast an update to refresh cart quantity in ProductListActivity
            Intent intent = new Intent("CART_UPDATED");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            // Run UI updates on the main thread
            runOnUiThread(() -> Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show());
        });
    }
}

