package com.example.shopgame.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table")
public class Order {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String productName;
    private int quantity;
    private double totalPrice;
    private String date;
    private int imageResId; // ID của ảnh sản phẩm
    private int userId; // Thêm userId để phân biệt đơn hàng của người dùng

    // Constructor với userId
    public Order(String productName, int quantity, double totalPrice, String date, int imageResId, int userId) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.date = date;
        this.imageResId = imageResId;
        this.userId = userId; // Thiết lập userId
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
