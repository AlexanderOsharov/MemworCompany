package com.shurik.memwor_24.pizza_planet.order_database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDAO {

    @Query("select * from `order`")
    List<OrderEntity> getAll(); // получение всех заказов

    @Insert
    void save(OrderEntity order); // сохраняем заказ

    @Delete
    void delete(OrderEntity order); // удаляем заказ
}