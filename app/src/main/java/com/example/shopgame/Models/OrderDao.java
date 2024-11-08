package com.example.shopgame.Models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    void insert(Order order);

    // Sử dụng đúng tên bảng order_table
    @Query("SELECT * FROM order_table ORDER BY date DESC")
    List<Order> getAllOrders();



    @Query("SELECT * FROM order_table WHERE userId = :userId")
    List<Order> getOrdersByUserId(int userId);
}
