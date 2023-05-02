package com.shurik.memwor_24.pizza_planet.model;

import com.shurik.memwor_24.pizza_planet.order_database.OrderEntity;

// класс пицы (которая лежит на сервере)
public class PizzaSev {

    private int id; // шв - шник

    private String title; // название

    private String description; // описание

    private PizzaSevPicture pizzaPicture; // картинка

    private int price; // цена

    private int quantity = 1; // количество

    private OrderEntity order; // заказ

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