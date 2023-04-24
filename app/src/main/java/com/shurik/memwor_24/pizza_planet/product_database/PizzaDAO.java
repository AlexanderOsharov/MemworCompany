package com.shurik.memwor_24.pizza_planet.product_database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PizzaDAO {

    @Query("SELECT * FROM pizza")
    List<PizzaEntity> getAllPizzas();

    @Insert
    void insert(PizzaEntity pizza);

    @Delete
    void delete(PizzaEntity pizza);

    @Update
    void update(PizzaEntity pizza);

}