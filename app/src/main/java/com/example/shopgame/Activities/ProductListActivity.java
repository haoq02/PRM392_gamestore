package com.example.shopgame.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.example.shopgame.Adapters.ProductAdapter;
import com.example.shopgame.Models.Product;
import com.example.shopgame.Models.ProductDao;
import com.example.shopgame.Models.CartDao;
import com.example.shopgame.Utils.AppDatabase;
import com.example.shopgame.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private Button cartButton, profileButton, showMapButton;
    private EditText searchBar;
    private Spinner priceFilterSpinner;
    private ProductAdapter productAdapter;
    private ProductDao productDao;
    private CartDao cartDao;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartButton = findViewById(R.id.cartButton);
        profileButton = findViewById(R.id.profileButton);
        showMapButton = findViewById(R.id.showMapButton);
        searchBar = findViewById(R.id.searchBar);
        priceFilterSpinner = findViewById(R.id.priceFilterSpinner);

        productDao = AppDatabase.getDatabase(this).productDao();
        cartDao = AppDatabase.getDatabase(this).cartDao();

        showMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, StoreMapActivity.class);
            startActivity(intent);
        });

        addSampleData();
        fetchProducts();
        updateCartQuantity();

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        priceFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(cartUpdateReceiver, new IntentFilter("CART_UPDATED"));
    }

    private void updateCartQuantity() {
        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("id", -1);
        if (userId == -1) {
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            int totalQuantity = cartDao.getTotalQuantityByUserId(userId);
            runOnUiThread(() -> cartButton.setText("Go to Cart (" + totalQuantity + ")"));
        });
    }

    private void addSampleData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (productDao.getAllProducts().isEmpty()) {
                List<Product> sampleProducts = new ArrayList<>();
                sampleProducts.add(new Product("PlayStation 5", 499.99, R.drawable.playstation_5, "Sony PlayStation 5 Console"));
                sampleProducts.add(new Product("Xbox Series X", 499.99, R.drawable.xbox_series_x, "Microsoft Xbox Series X Console"));
                sampleProducts.add(new Product("Nintendo Switch", 299.99, R.drawable.nintendo_switch, "Nintendo Switch Console"));
                sampleProducts.add(new Product("Nintendo Switch Lite", 199.99, R.drawable.switch_lite, "Nintendo Switch Lite Console"));
                sampleProducts.add(new Product("PlayStation 5 Spider-Man 2 Limited Edition", 1999.99, R.drawable.ps5_spiderman, "Sony PlayStation 5 Console"));
                sampleProducts.add(new Product("ASUS ROG Ally", 899.99, R.drawable.rog_ally, "ROG Ally Gaming PC Console"));
                for (Product product : sampleProducts) {
                    productDao.insert(product);
                }
            }
        });
    }

    private void fetchProducts() {
        Executors.newSingleThreadExecutor().execute(() -> {
            allProducts = productDao.getAllProducts();
            filteredProducts.addAll(allProducts);
            runOnUiThread(() -> {
                productAdapter = new ProductAdapter(this, filteredProducts);
                productRecyclerView.setAdapter(productAdapter);
            });
        });
    }

    private void filterProducts() {
        String query = searchBar.getText().toString().toLowerCase();
        String priceRange = priceFilterSpinner.getSelectedItem().toString();

        filteredProducts.clear();

        for (Product product : allProducts) {
            boolean matchesName = product.getName().toLowerCase().contains(query);
            boolean matchesPrice = false;
            double price = product.getPrice();

            switch (priceRange) {
                case "All":
                    matchesPrice = true;
                    break;
                case "Under $500":
                    matchesPrice = price < 500;
                    break;
                case "$500 - $1000":
                    matchesPrice = price >= 500 && price <= 1000;
                    break;
                case "Above $1000":
                    matchesPrice = price > 1000;
                    break;
            }

            if (matchesName && matchesPrice) {
                filteredProducts.add(product);
            }
        }

        productAdapter.updateProductList(filteredProducts);
    }

    private final BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartQuantity();
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(cartUpdateReceiver);
        super.onDestroy();
    }
}
