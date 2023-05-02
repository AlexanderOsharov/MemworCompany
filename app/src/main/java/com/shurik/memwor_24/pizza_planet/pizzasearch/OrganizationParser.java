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
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// парсер кафe / организаций
public class OrganizationParser {

    // Метод для получения ближайших организаций
    public static List<Organization> getOrganizations(Location location) throws IOException {

        // создаем List для организаций / кафе
        List<Organization> organizations = new ArrayList<>();

        // Дальше идет запрос

        OkHttpClient client = new OkHttpClient();
        /**
         * OkHttpClient - клиент для HTTP-вызовов,
         * который можно использовать для отправки запросов и чтения ответов
         */

        // todo задать сане вопрос
        // Класс Request представляет собой HTTP-запрос
        Request request = new Request.Builder()
                .url(
                        "https://search-maps.yandex.ru/v1/?text=пицца,кафе,ресторан,Москва&type=biz&ll="
                                + location.getLatitude() // широта
                                + ","
                                + location.getLongitude() // долгота
                                + "&spn=0.552069,0.400552&lang=ru_RU&results=10&apikey=f07e4006-27b1-4c34-ab91-37e3314ad93d")
                .build();
        // Задаем целевой URL-адрес этого запроса.

        Response response = client.newCall(request).execute();
        /**
         * Что происходит в этой строчке по шагам:
         * 1. создаем HTTP-ответ;
         * 2. newCall() - метод, который подготавливает запрос - request -
         * к выполнению (в какой-то момент в будущем);
         * 3. execute() - вызывает запрос и блокирует до тех пор,
         * пока ответ не будет обработан или не окажется ошибочным.
         */


        // Парсим address, id, name

        assert response.body() != null;

        JsonObject object = JsonParser.parseString(
                        response.body().string())
                .getAsJsonObject();
        /**
         * Сохранем нужные нам даннные (которы мы дсотаем из запроса)
         * в объект типа JsonObject
         */

        JsonArray features = (JsonArray) object.get("features");
        // features - особенности

        for (JsonElement item : features) {

            JsonObject properties = (JsonObject) item
                    .getAsJsonObject()
                    .get("properties");

            JsonObject companyMetaData = (JsonObject) properties
                    .get("CompanyMetaData");

            // сохраняем адресс
            String address = String.valueOf(companyMetaData
                    .get("address")).replace("\"", "");
            // сохраняем название
            String name = String.valueOf(companyMetaData
                    .get("name")).replace("\"", "");

            // сохраняем id организации
            String id = String.valueOf(companyMetaData
                    .get("id")).replace("\"", "");

            JsonObject geometry = (JsonObject) item
                    .getAsJsonObject().get("geometry");

            JsonArray coordinates = (JsonArray)
                    geometry.get("coordinates");

            // получаем координаты
            String latitude = String.valueOf(
                    coordinates.get(1));
            String longitude = String.valueOf(
                    coordinates.get(0));

            // int id
            int mId = Integer.parseInt(id);

            // получаем пиццы, которые есть в ресторане (по его id)
            List<Pizza> pizzaList = PizzaParser.getPizzaList(id);

            if (pizzaList != null) { // если пица всее - таки есть
                organizations.add(new Organization(
                        mId,
                        name,
                        address,
                        latitude,
                        longitude,
                        pizzaList));
            }
        }
        return organizations;
    }
}