package com.shurik.memwor_24.pizza_planet;

import android.app.Application;

import androidx.room.Room;

import com.shurik.memwor_24.pizza_planet.order_database.OrderDatabase;
import com.shurik.memwor_24.pizza_planet.user_database.UserDatabase;

public class PizzaPlanetDatabaseApplication extends Application {

    private static PizzaPlanetDatabaseApplication instance;

    private static UserDatabase userDatabase;

    private static OrderDatabase orderDatabase;

    public static PizzaPlanetDatabaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        userDatabase = Room.databaseBuilder(this,
                UserDatabase.class,
                UserDatabase.NAME).fallbackToDestructiveMigration().build();

        orderDatabase = Room.databaseBuilder(this,
                OrderDatabase.class,
                OrderDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public static UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public static OrderDatabase getOrderDatabase() {
        return orderDatabase;
    }
}