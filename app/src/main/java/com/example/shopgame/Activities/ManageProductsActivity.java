package com.example.shopgame.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopgame.Adapters.ManageProductsAdapter;
import com.example.shopgame.Models.Product;
import com.example.shopgame.Models.ProductDao;
import com.example.shopgame.R;
import com.example.shopgame.Utils.AppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class ManageProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private ManageProductsAdapter adapter;
    private ProductDao productDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productDao = AppDatabase.getDatabase(this).productDao();

        loadProducts();
    }

    private void loadProducts() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Product> productList = productDao.getAllProducts(); // Assumes you have this method in ProductDao
            runOnUiThread(() -> {
                adapter = new ManageProductsAdapter(this, productList, productDao);
                productsRecyclerView.setAdapter(adapter);
            });
        });
    }
}
