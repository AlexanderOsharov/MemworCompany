package com.shurik.memwor_24.pizza_planet.model;

public class PizzaSevPicture {

    private int id;

    private String pizzaUri;

    public PizzaSevPicture(int id, String pizzaUri) {
        this.id = id;
        this.pizzaUri = pizzaUri;
    }

    public PizzaSevPicture(String pizzaUri) {
        this.pizzaUri = pizzaUri;
    }

    public int getId() {
        return id;
    }

    public String getPizzaUri() {
        return pizzaUri;
    }
}