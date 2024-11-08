package com.example.shopgame.Models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Query("SELECT * FROM product_table WHERE id = :id")
    Product getProductById(int id);

    @Query("SELECT * FROM product_table")
    List<Product> getAllProducts();

    @Delete
    void delete(Product product); // Thêm phương thức delete
}
