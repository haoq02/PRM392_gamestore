package com.example.shopgame.Models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("DELETE FROM cart_table")
    void clearCart();

    @Query("SELECT * FROM cart_table")
    List<CartItem> getAllCartItems();

    // Add this method to fetch a cart item by name
    @Query("SELECT * FROM cart_table WHERE name = :name LIMIT 1")
    CartItem getCartItemByName(String name);

    @Query("SELECT SUM(quantity) FROM cart_table")
    int getTotalQuantity();



    @Query("SELECT * FROM cart_table WHERE userId = :userId")
    List<CartItem> getCartItemsByUserId(int userId);

    @Query("DELETE FROM cart_table WHERE userId = :userId")
    void clearCartByUserId(int userId);

    @Query("SELECT SUM(quantity) FROM cart_table WHERE userId = :userId")
    int getTotalQuantity(int userId);

    @Query("SELECT * FROM cart_table WHERE name = :name AND userId = :userId LIMIT 1")
    CartItem getCartItemByNameAndUserId(String name, int userId);

    @Query("SELECT SUM(quantity) FROM cart_table WHERE userId = :userId")
    int getTotalQuantityByUserId(int userId);



}

