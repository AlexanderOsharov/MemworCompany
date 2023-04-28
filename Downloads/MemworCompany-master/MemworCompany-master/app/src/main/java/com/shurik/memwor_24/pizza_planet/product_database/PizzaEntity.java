package com.shurik.memwor_24.pizza_planet.product_database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pizza")
public class PizzaEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // название пиццы
    private String title;

    // описание
    private String desciption;

    // изображение
    private String pic;

    // цена
    private String fee;

    // количество
    private int quantity = 1;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}