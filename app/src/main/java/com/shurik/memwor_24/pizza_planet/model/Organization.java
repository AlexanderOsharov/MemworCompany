package com.shurik.memwor_24.pizza_planet.model;

import java.util.List;

// класс организации / кафе / ресторана
public class Organization {

    // id - шник
    private int id;

    // название кафе / ресторана
    private String name;

    // адресс кафе / ресторана
    private String address;

    // координаты кафе
    private String latitude; // широта
    private String longitude; // долгота

    // список пицц в данном кафе / ресторане
    private List<Pizza> pizzaList;

    public Organization(int id,
                        String name,
                        String address,
                        String latitude,
                        String longitude,
                        List<Pizza> pizzaList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pizzaList = pizzaList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public List<Pizza> getPizzaList() {
        return pizzaList;
    }
}