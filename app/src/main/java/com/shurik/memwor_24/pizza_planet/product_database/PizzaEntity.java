package com.shurik.memwor_24.pizza_planet.product_database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "pizza")
public class PizzaEntity {
    // класс пиццы

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    @NotNull
    private String title; // название пиццы

    @ColumnInfo(name = "description")
    @NotNull
    private String desciption; // описание пиццы

    @ColumnInfo(name = "pic")
    @NotNull
    private String pic; // изображение

    @ColumnInfo(name = "fee")
    @NotNull
    private String fee; // цена

    @ColumnInfo(name = "quantity")
    private int quantity = 1; // количество (по умолчанию - 1)

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public void setDesciption(@NotNull String desciption) {
        this.desciption = desciption;
    }

    public void setPic(@NotNull String pic) {
        this.pic = pic;
    }

    public void setFee(@NotNull String fee) {
        this.fee = fee;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    @NotNull
    public String getDesciption() {
        return desciption;
    }

    @NotNull
    public String getPic() {
        return pic;
    }

    @NotNull
    public String getFee() {
        return fee;
    }

    public int getQuantity() {
        return quantity;
    }
}