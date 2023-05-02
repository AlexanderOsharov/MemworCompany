package com.shurik.memwor_24.pizza_planet.model;

// класс картинки пиццы
public class PizzaSevPicture {

    private int pizzaPicId; // id

    private String pizzaUri; // uri картинки

    public PizzaSevPicture(int pizzaPicId, String pizzaUri) {
        this.pizzaPicId = pizzaPicId;
        this.pizzaUri = pizzaUri;
    }

    public PizzaSevPicture(String pizzaUri) {
        this.pizzaUri = pizzaUri;
    }

    public int getId() {
        return pizzaPicId;
    }

    public String getPizzaUri() {
        return pizzaUri;
    }
}