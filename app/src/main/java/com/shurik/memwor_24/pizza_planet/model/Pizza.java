package com.shurik.memwor_24.pizza_planet.model;

public class Pizza {
    // id (номер)
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Pizza(String title, String desciption, String pic, String fee) {
        this.title = title;
        this.desciption = desciption;
        this.pic = pic;
        this.fee = fee;
    }

    public Pizza(String title, String desciption, String pic, String fee, int quantity) {
        this.title = title;
        this.desciption = desciption;
        this.pic = pic;
        this.fee = fee;
        this.quantity = quantity;
    }

    public Pizza(int id, String title, String desciption, String pic, String fee, int quantity) {
        this.id = id;
        this.title = title;
        this.desciption = desciption;
        this.pic = pic;
        this.fee = fee;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return desciption;
    }

    public String getPic() {
        return pic;
    }

    public String getFee() {
        return fee;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String desciption) {
        this.desciption = desciption;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
