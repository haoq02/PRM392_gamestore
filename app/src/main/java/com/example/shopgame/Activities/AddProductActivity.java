package com.example.shopgame.Activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shopgame.Models.Product;
import com.example.shopgame.Models.ProductDao;
import com.example.shopgame.R;
import com.example.shopgame.Utils.AppDatabase;

import java.util.concurrent.Executors;

public class AddProductActivity extends AppCompatActivity {

    private EditText productNameEditText, productPriceEditText, productImageEditText, productDescriptionEditText;
    private Button addProductButton;
    private ProductDao productDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameEditText = findViewById(R.id.productNameEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productImageEditText = findViewById(R.id.productImageEditText); // Lấy tên ảnh từ drawable
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        addProductButton = findViewById(R.id.addProductButton);

        productDao = AppDatabase.getDatabase(this).productDao();

        addProductButton.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        String name = productNameEditText.getText().toString().trim();
        String priceText = productPriceEditText.getText().toString().trim();
        String imageName = productImageEditText.getText().toString().trim(); // Lấy tên ảnh
        String description = productDescriptionEditText.getText().toString().trim();

        if (name.isEmpty() || priceText.isEmpty() || imageName.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ID ảnh từ tên ảnh
        int imageResId = getImageResourceId(this, imageName);

        if (imageResId == 0) {
            Toast.makeText(this, "Invalid image name", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(name, price, imageResId, description);
        Executors.newSingleThreadExecutor().execute(() -> {
            productDao.insert(product);
            runOnUiThread(() -> Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show());
        });
    }

    private int getImageResourceId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }
}
