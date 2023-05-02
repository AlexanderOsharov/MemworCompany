package com.shurik.memwor_24.pizza_planet.model;

// класс пиццы
public class Pizza {

    // id - шник пиццы
    private int id;

    // название пиццы
    private String title;

    // описание
    private String desciption;

    // картинка
    private String pic;

    // цена
    private String fee;

    // количество
    private int quantity = 1;

    public Pizza(int id,
                 String title,
                 String desciption,
                 String pic,
                 String fee,
                 int quantity) {
        this.id = id;
        this.title = title;
        this.desciption = desciption;
        this.pic = pic;
        this.fee = fee;
        this.quantity = quantity;
    }

    public Pizza(String title,
                 String desciption,
                 String pic,
                 String fee) {
        this.title = title;
        this.desciption = desciption;
        this.pic = pic;
        this.fee = fee;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // TODO ЕСЛИ ЧТО - ДОПИШЕМ СЕТТЕРЫ

    public int getId() {
        return id;
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

    public int getQuantity() {
        return quantity;
    }
}