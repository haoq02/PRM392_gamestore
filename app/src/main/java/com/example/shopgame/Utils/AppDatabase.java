package com.example.shopgame.Utils;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.shopgame.Models.CartItem;
import com.example.shopgame.Models.CartDao;
import com.example.shopgame.Models.Order;
import com.example.shopgame.Models.OrderDao;
import com.example.shopgame.Models.Product;
import com.example.shopgame.Models.ProductDao;
import com.example.shopgame.Models.User;
import com.example.shopgame.Models.UserDao;

@Database(entities = {User.class, Product.class, CartItem.class, Order.class, }, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract CartDao cartDao();
    public abstract OrderDao orderDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "shop_game_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
