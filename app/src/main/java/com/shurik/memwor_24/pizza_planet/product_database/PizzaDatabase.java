package com.shurik.memwor_24.pizza_planet.product_database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PizzaEntity.class}, version = 1)
public abstract class PizzaDatabase extends RoomDatabase {

    private static PizzaDatabase instance;

    public abstract PizzaDAO pizzaDao();

    public static synchronized PizzaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PizzaDatabase.class, "pizza_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}