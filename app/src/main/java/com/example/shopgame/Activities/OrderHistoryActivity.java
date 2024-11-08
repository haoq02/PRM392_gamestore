package com.example.shopgame.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopgame.Adapters.OrderAdapter;
import com.example.shopgame.Models.Order;
import com.example.shopgame.Models.OrderDao;
import com.example.shopgame.R;
import com.example.shopgame.Utils.AppDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private OrderDao orderDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderDao = AppDatabase.getDatabase(this).orderDao();

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", -1); // Lấy userId từ SharedPreferences

        if (userId == -1) {
            // Nếu không tìm thấy userId, có thể xử lý lỗi ở đây
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Order> orderList = orderDao.getOrdersByUserId(userId); // Truy vấn các đơn hàng theo userId
            runOnUiThread(() -> {
                orderAdapter = new OrderAdapter(this, orderList);
                orderRecyclerView.setAdapter(orderAdapter);
            });
        });
    }
}
