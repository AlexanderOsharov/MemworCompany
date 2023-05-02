package com.shurik.memwor_24.pizza_planet.user_database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.shurik.memwor_24.pizza_planet.model.Phone;

@Dao
public interface UserDAO {

    @Query("select * from user where phone = :phone")
    UserEntity getUser(Phone phone); // поиск пользователя в базе данных по id - шнику

    @Insert
    void save(UserEntity user); // сохранение пользователя

    @Update
    void update(UserEntity user); // обновление юзера
}