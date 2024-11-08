package com.example.shopgame;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo Stetho
        Stetho.initializeWithDefaults(this);
    }
}
