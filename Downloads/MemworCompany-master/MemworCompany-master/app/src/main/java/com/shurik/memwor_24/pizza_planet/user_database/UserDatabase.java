package com.shurik.memwor_24.pizza_planet.user_database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public static final String NAME = "user";

    public abstract UserDAO userDao();
}