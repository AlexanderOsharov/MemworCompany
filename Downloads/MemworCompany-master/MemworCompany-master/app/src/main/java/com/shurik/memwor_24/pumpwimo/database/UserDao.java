package com.shurik.memwor_24.pumpwimo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from users")
    List<User> getAll(); // получение всех пользователей

    @Insert
     void save(User user); // сохранение пользователя

    @Update
    void update(User user); // обновление данных юзера

    @Delete
    void delete(User user); // удаление узера

    @Query(value = "SELECT * FROM users WHERE Password = :s")
    User[] search(String s); // поиск по паролю
}