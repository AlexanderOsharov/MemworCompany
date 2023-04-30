package com.shurik.memwor_24.pizza_planet.user_database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface UserDAO {

    @Insert
    void save(UserEntity user); // сохранение пользователя

    @Update
    void update(UserEntity user); // обновление юзера
}