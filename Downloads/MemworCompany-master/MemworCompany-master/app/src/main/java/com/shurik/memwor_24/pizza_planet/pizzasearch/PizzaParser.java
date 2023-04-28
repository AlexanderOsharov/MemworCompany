package com.shurik.memwor_24.pizza_planet.pizzasearch;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shurik.memwor_24.pizza_planet.model.Pizza;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PizzaParser {

    // Метод для получения массива пицц (названия, описания, цены)
    public static ArrayList<Pizza> getPizzas(String id) throws IOException {
        ArrayList<Pizza> pizzas = new ArrayList<>();
        Document doc = Jsoup.connect(getOrganizationPage(id)).get();
        if (isContainsMenu(doc)) {
            if (isContainsPizza(doc)) {
                Elements categories = doc.getElementsByClass("business-full-items-grouped-view__category");
                for (Element item : categories) {
                    if (isPizzaСategory(item)) {
                        Elements info = item.getElementsByClass("related-product-view _size_normal");
                        for (Element e : info) {
                            pizzas.add(new Pizza(e.select(".related-item-photo-view__title").text(),
                                    e.select(".related-item-photo-view__description").text(),
                                    "https://e0.edimdoma.ru/data/posts/0002/1429/21429-ed4_wide.jpg?1631194036",
                                    e.select(".related-product-view__price").text()));
                        }
                    }
                }
            } else {
                System.out.println("К сожалению, на странице организации отсутствует пицца.");
                return null;
            }
        } else {
            System.out.println("К сожалению, на странице организации отсутствует меню.");
            return null;
        }
        return pizzas;
    }

    // Метод для получения id заведения
    private static String getId(String organization) throws IOException {
        // Получение информации о заведении в json формате
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url("https://search-maps.yandex.ru/v1/?text=" + organization + ",Волгоград&type=biz&lang=ru_RU&results=1&apikey=f07e4006-27b1-4c34-ab91-37e3314ad93d").
                build();
        Response response = client.newCall(request).execute();

        // Получение id органзации
        assert response.body() != null;
        JsonObject object = JsonParser.parseString(response.body().string()).getAsJsonObject();
        JsonArray features = (JsonArray) object.get("features");
        JsonObject item = (JsonObject) features.get(0);
        JsonObject properties = (JsonObject) item.get("properties");
        JsonObject companyMetaData = (JsonObject) properties.get("CompanyMetaData");
        return String.valueOf(companyMetaData.get("id")).replace("\"", "");
    }

    // Метод для формирования ссылки организации по переданному id
    private static String getOrganizationPage (String id) {
        return "https://yandex.ru/maps/org/" + id + "/menu";
    }

    // Метод для проверки наличия меню на странице организации
    private static boolean isContainsMenu(Document doc) {
        Elements menu = doc.select("div.carousel__content");
        return menu.select("a").text().contains("Меню");
    }

    // Метод для проверки наличия пиццы на странице организации
    private static boolean isContainsPizza(Document doc) {
        Elements categories = doc.getElementsByClass("business-full-items-grouped-view__category");
        for (Element item : categories) {
            if (item.select(".business-full-items-grouped-view__title").text().equals("Пицца") ||
                    item.select(".business-full-items-grouped-view__title").text().equals("Пиццa")) {
                return true;
            }
        }
        return false;
    }

    // Метод для проверки наличия пиццы на странице организации
    private static boolean isPizzaСategory (Element item) {
        return item.select(".business-full-items-grouped-view__title").text().equals("Пицца") ||
                item.select(".business-full-items-grouped-view__title").text().equals("Пиццa");
    }
}
