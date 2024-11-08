package com.example.shopgame.Adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.shopgame.Models.CartDao;
import com.example.shopgame.Models.CartItem;
import com.example.shopgame.R;
import com.example.shopgame.Utils.AppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItem> cartItems;
    private final Context context;
    private final CartDao cartDao;
    private final Runnable updateTotalPrice;

    public CartAdapter(Context context, List<CartItem> cartItems, Runnable updateTotalPrice) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartDao = AppDatabase.getDatabase(context).cartDao();
        this.updateTotalPrice = updateTotalPrice;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.nameTextView.setText(cartItem.getName());
        holder.priceTextView.setText("$" + cartItem.getPrice());
        holder.quantityTextView.setText("Qty: " + cartItem.getQuantity());
        holder.productImageView.setImageResource(cartItem.getImageResId()); // Assumes an image resource ID

        // Lấy userId từ SharedPreferences (hoặc nguồn phù hợp)
        int userId = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).getInt("id", -1);
        if (userId == -1) {
            // Nếu không có userId hợp lệ, bạn có thể xử lý bằng cách hiển thị lỗi hoặc không cho phép thao tác
            return;
        }

        // Nút tăng số lượng
        holder.increaseButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setUserId(userId); // Cập nhật userId trước khi lưu
            updateCartItem(cartItem);
            notifyItemChanged(position);
            // Gửi Intent thông báo rằng giỏ hàng đã được cập nhật
            Intent intent = new Intent("CART_UPDATED");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        });

        // Nút giảm số lượng
        holder.decreaseButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItem.setUserId(userId); // Cập nhật userId trước khi lưu
                updateCartItem(cartItem);
                notifyItemChanged(position);
                // Gửi Intent thông báo rằng giỏ hàng đã được cập nhật
                Intent intent = new Intent("CART_UPDATED");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        // Nút xóa sản phẩm khỏi giỏ hàng
        holder.deleteButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                cartItem.setUserId(userId); // Cập nhật userId trước khi xóa
                cartDao.delete(cartItem);
                cartItems.remove(position);

                // Sử dụng Handler để chạy trên luồng chính
                new Handler(Looper.getMainLooper()).post(() -> {
                    notifyItemRemoved(position);
                    updateTotalPrice.run(); // Cập nhật tổng giá

                    // Gửi Intent thông báo rằng giỏ hàng đã được cập nhật
                    Intent intent = new Intent("CART_UPDATED");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                });
            });
        });
    }


    private void updateCartItem(CartItem cartItem) {
        Executors.newSingleThreadExecutor().execute(() -> {
            cartDao.update(cartItem);
            new Handler(Looper.getMainLooper()).post(updateTotalPrice);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // Trả về danh sách các cart items
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView, quantityTextView;
        ImageView productImageView;
        Button increaseButton, decreaseButton, deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cartProductNameTextView);
            priceTextView = itemView.findViewById(R.id.cartProductPriceTextView);
            quantityTextView = itemView.findViewById(R.id.cartProductQuantityTextView);
            productImageView = itemView.findViewById(R.id.cartProductImageView);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
