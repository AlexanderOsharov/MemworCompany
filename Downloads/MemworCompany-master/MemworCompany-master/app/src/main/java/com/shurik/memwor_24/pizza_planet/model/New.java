package com.shurik.memwor_24.pizza_planet.model;

// класс новости
public class New {

    // название новости
    private String sNew;

    // картинка
    private int pic;

    // иконка пиццы
    private int pizza_icon;

    // описание нвоости
    private String description;

    public New(String sNew, int pic, int pizza_icon, String description) {
        this.sNew = sNew;
        this.pic = pic;
        this.pizza_icon = pizza_icon;
        this.description = description;
    }

    public String getsNew() {
        return sNew;
    }

    public int getPic() {
        return pic;
    }

    public String getDescription() {
        return description;
    }

    public int getPizza_icon() {
        return pizza_icon;
    }
}

