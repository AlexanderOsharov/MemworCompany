package com.shurik.memwor_24.pizza_planet.mapper;

import com.shurik.memwor_24.pizza_planet.order_database.OrderEntity;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderMapper {
    public static OrderEntity orderFromJson(JSONObject jsonObject) {
        OrderEntity order = null;

        try {
            order = new OrderEntity(
                    jsonObject.getLong("id"), // id -шник
                    UserMapper.userFromJson(jsonObject), // заказчик
                    jsonObject.getString("customerAddress"), // адресс заказчика
                    PizzaListMapper.pizzaListFromJson(jsonObject), // список пицц
                    jsonObject.getInt("completed") // завершен или нет
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return order;
    }
}