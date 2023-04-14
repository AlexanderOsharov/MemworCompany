package com.shurik.pizzaplanet.model;

import java.util.ArrayList;
import java.util.List;

// класс организации
public class Organization {

    // адресс организации / заведения
    private String address;

    // название организации / заведения
    private String name;

    // id - шник организации / заведения
    private String id;

    // список пицц
    private List<Pizza> pizzas;

    public Organization(String address, String name, String id) {
        this.address = address;
        this.name = name;
        this.id = id;
    }

    public Organization(String address, String name, String id, String html, ArrayList<Pizza> pizzas) {
        this.address = address;
        this.name = name;
        this.id = id;
        this.pizzas = pizzas;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Addres: " + address + "\n" +
                "Name: " + name + "\n" +
                "ID: " + id + "\n" +
                "Pizza" + pizzas.toString();
    }

    public List<Pizza> getPizzaList() {
        return pizzas;
    }
}
