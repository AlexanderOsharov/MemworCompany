package com.shurik.memwor_24.pumpwimo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public static final String NAME = "Users";
    public abstract UserDao userDao();
}
