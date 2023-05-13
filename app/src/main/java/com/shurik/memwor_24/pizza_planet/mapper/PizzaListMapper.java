//package com.shurik.memwor_24.pizza_planet.mapper;
//
//import com.shurik.memwor_24.pizza_planet.model.PizzaSev;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.List;
//
//public class PizzaListMapper {
//
//    // получаем список пицц из json
//    public static List<PizzaSev> pizzaListFromJson(JSONObject jsonObject) throws JSONException {
//        List<PizzaSev> pizzaList = null;
//        pizzaList = (List<PizzaSev>) jsonObject.getJSONArray("pizzaList");
//        return pizzaList;
//    }
//}