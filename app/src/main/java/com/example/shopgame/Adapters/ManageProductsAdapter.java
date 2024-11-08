package com.example.shopgame.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopgame.Models.Product;
import com.example.shopgame.Models.ProductDao;
import com.example.shopgame.R;

import java.util.List;
import java.util.concurrent.Executors;

public class ManageProductsAdapter extends RecyclerView.Adapter<ManageProductsAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> productList;
    private final ProductDao productDao;

    public ManageProductsAdapter(Context context, List<Product> productList, ProductDao productDao) {
        this.context = context;
        this.productList = productList;
        this.productDao = productDao;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_manage, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText("$" + product.getPrice());
        holder.productDescriptionTextView.setText(product.getDescription());
        holder.productImageView.setImageResource(product.getImageResId());

        holder.editButton.setOnClickListener(v -> {
            // Handle edit functionality
        });

        holder.deleteButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                productDao.delete(product);
                productList.remove(position);

                // Sử dụng Handler để chạy trên luồng chính
                new Handler(Looper.getMainLooper()).post(() -> notifyItemRemoved(position));
            });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, productPriceTextView, productDescriptionTextView;
        ImageView productImageView;
        Button editButton, deleteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productDescriptionTextView = itemView.findViewById(R.id.productDescriptionTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
