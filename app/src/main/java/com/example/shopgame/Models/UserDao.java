package com.example.shopgame.Models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("UPDATE user_table SET password = :newPassword WHERE email = :email")
    void updatePassword(String email, String newPassword);

    @Query("UPDATE user_table SET fullName = :newFullName, phoneNumber = :newPhone, address = :newAddress WHERE email = :userEmail")
    void updateUserInfo(String userEmail, String newFullName, String newPhone, String newAddress);

    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    User getUserById(int userId);



    @Update
    void update(User user);
    // Thêm các phương thức liên quan đến yêu thích



}
