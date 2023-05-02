package com.shurik.memwor_24.pizza_planet.model;

import com.shurik.memwor_24.pizza_planet.order_database.OrderEntity;

public class PizzaSev {
    // класс пицы (которая лежит на сервере)

    private int id;

    private String title;

    private String description;

    private PizzaSevPicture pizzaPicture;

    private int price;

    private int quantity = 1;

    private OrderEntity order;

    public PizzaSev(int id, String title,
                    String description, PizzaSevPicture pizzaPicture,
                    int price, int quantity, OrderEntity order) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pizzaPicture = pizzaPicture;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
    }

    public PizzaSev(String title,
                    String description, PizzaSevPicture pizzaPicture,
                    int price, int quantity, OrderEntity order) {
        this.title = title;
        this.description = description;
        this.pizzaPicture = pizzaPicture;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PizzaSevPicture getPizzaPicture() {
        return pizzaPicture;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderEntity getOrder() {
        return order;
    }
}