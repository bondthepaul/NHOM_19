package com.example.nhom_19;

import android.app.Application;

public class App extends Application {
    DBHelper dbHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);
        dbHelper.createTable();
    }
}
