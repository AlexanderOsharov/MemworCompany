package com.shurik.memwor_24.pumpwimo.database;

import android.app.Application;

import androidx.room.Room;

public class UserDatabaseApplication extends Application {

    private static UserDatabaseApplication instance;

    public static UserDatabaseApplication getInstance() {
        return instance;
    }

    private static UserDatabase userDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        userDatabase = Room.databaseBuilder(this, UserDatabase.class, UserDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public static UserDatabase getUserDatabase() {
        return userDatabase;
    }
}