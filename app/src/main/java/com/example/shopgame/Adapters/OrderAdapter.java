package com.example.shopgame.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopgame.Models.Order;
import com.example.shopgame.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private Context context;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.productNameTextView.setText(order.getProductName());
        holder.quantityTextView.setText("Quantity: " + order.getQuantity());
        holder.totalPriceTextView.setText("Total: $" + String.format("%.2f", order.getTotalPrice()));
        holder.dateTextView.setText("Date: " + order.getDate());
        holder.productImageView.setImageResource(order.getImageResId()); // Hiển thị ảnh sản phẩm
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, quantityTextView, totalPriceTextView, dateTextView;
        ImageView productImageView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
        }
    }
}
