package com.shurik.memwor_24.pizza_planet.model;

import java.util.ArrayList;
import java.util.List;

public class Organization {
    private String name;
    private String address;
    private String id;
    private String latitude;
    private String longitude;
    private List<Pizza> pizzas;

    public Organization(String address, String name, String id) {
        this.address = address;
        this.name = name;
        this.id = id;
    }


    public Organization(String address, String name, String id, ArrayList<Pizza> pizzas, String latitude, String longitude) {
        this.address = address;
        this.name = name;
        this.id = id;
        this.pizzas = pizzas;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
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

    public List<Pizza> getPizzaList() {
        return pizzas;
    }
}
