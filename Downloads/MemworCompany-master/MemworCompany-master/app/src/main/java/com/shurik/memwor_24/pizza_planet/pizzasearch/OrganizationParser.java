package com.shurik.memwor_24.pizza_planet.pizzasearch;

import android.location.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shurik.memwor_24.pizza_planet.model.Organization;
import com.shurik.memwor_24.pizza_planet.model.Pizza;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrganizationParser {

    // Метод для получения ближайших организаций
    public static ArrayList<Organization> getOrganizations(Location location) throws IOException {

        // создаем ArrayList для организаций
        ArrayList<Organization> organizations = new ArrayList<>();

        // Запрос
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url(
                        "https://search-maps.yandex.ru/v1/?text=пицца,кафе,ресторан,Москва&type=biz&ll=" +
                        location.getLatitude() +
                        "," +
                        location.getLongitude() +
                        "&spn=0.552069,0.400552&lang=ru_RU&results=10&apikey=f07e4006-27b1-4c34-ab91-37e3314ad93d")
                .build();

        Response response = client.newCall(request).execute();

        // Парсинг address, id, name
        assert response.body() != null;

        JsonObject object = JsonParser.parseString(response.body().string()).getAsJsonObject();
        JsonArray features = (JsonArray) object.get("features");
        for (JsonElement item : features) {
            JsonObject properties = (JsonObject) item.getAsJsonObject().get("properties");
            JsonObject companyMetaData = (JsonObject) properties.get("CompanyMetaData");
            String address = String.valueOf(companyMetaData.get("address")).replace("\"", "");
            String name = String.valueOf(companyMetaData.get("name")).replace("\"", "");
            String id = String.valueOf(companyMetaData.get("id")).replace("\"", "");

            JsonObject geometry = (JsonObject) item.getAsJsonObject().get("geometry");
            JsonArray coordinates = (JsonArray) geometry.get("coordinates");
            String latitude = String.valueOf(coordinates.get(1));
            String longitude = String.valueOf(coordinates.get(0));

            ArrayList<Pizza> pizzas = PizzaParser.getPizzas(id);

            if (pizzas != null) {
                organizations.add(new Organization(address, name, id, pizzas, latitude, longitude));
            }
        }

        return organizations;
    }
}
