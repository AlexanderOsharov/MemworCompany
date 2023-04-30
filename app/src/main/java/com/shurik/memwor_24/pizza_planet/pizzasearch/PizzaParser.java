package com.shurik.memwor_24.pizza_planet.pizzasearch;

import com.shurik.memwor_24.pizza_planet.model.Pizza;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PizzaParser {

    // Метод для получения списка пицц (названия, описания, цены)
    public static List<Pizza> getPizzaList(String id) throws IOException {

        // создание списка пицц
        List<Pizza> pizzaList = new ArrayList<>();

        Document doc = Jsoup.connect(getOrganizationPage(id)).get();
        /**
         *  вставлем ссылку на местоположение организации на Яндекс Картах,
         *  Сохраняем все данные в объект типа Document
         */

        if (isContainsMenu(doc)) { // если у организации есть меню

            if (isContainsPizza(doc)) { // если в этом меню есть пицца

                Elements categories = doc.
                        getElementsByClass("business-full-items-grouped-view__category");
                // записываем все элементы класса категорий в categories

                for (Element item : categories) { // "бегаем" по категориям

                    if (isPizzaСategory(item)) { // в item есть пицца?
                        Elements info = item.
                                getElementsByClass("related-product-view _size_normal");

                        for (Element e : info) {
                            pizzaList.add(
                                    new Pizza(
                                            e.select(".related-item-photo-view__title").text(), // Название пиццы
                                            e.select(".related-item-photo-view__description").text(), // Описание пицыы
                                            "https://e0.edimdoma.ru/data/posts/0002/1429/21429-ed4_wide.jpg?1631194036", // картинка пиццы TODO пока тоько одна
                                            e.select(".related-product-view__price").text() // цена пиццы
                                    )
                            );
                        }

                    }

                }

            } else return null; // нет пиццы - возвращаем null

        } else return null; // нет меню - возвращаем null

        return pizzaList; // если же все есть - возвращаем список пицц
    }

    /**
     * Метод для формирования ссылки организации
     * (ссылки на местоположнение ресторана на Яндекс Картах)
     * по переданному id
     */
    private static String getOrganizationPage(String id) {
        return "https://yandex.ru/maps/org/" + id + "/menu";
    }

    // Метод для проверки наличия меню на странице организации
    private static boolean isContainsMenu(Document doc) {
        Elements menu = doc.select("div.carousel__content");
        // берем из документа определенный блок - информация о кафе
        // сохраняем в объект типа Elements

        // проверяем, содержит ли инфа меню кафе
        return menu.select("a") // TODO ПОЧЕМУ select("a")?
                .text()
                .contains("Меню");
    }

    // Метод для проверки наличия пиццы в меню организации
    private static boolean isContainsPizza(Document doc) {
        Elements categories = doc
                .getElementsByClass("business-full-items-grouped-view__category");
        // записываем все элементы класса категорий в categories

        for (Element item : categories) { // "пробегаемся по категориям"
            String str = item
                    .select(".business-full-items-grouped-view__title").text();
            // записываем в str название категории

            if (str.equals("Пицца")) return true; // если категори - пицца, то возвращаем true
        }
        return false; // нет - false
    }

    // Метод для проверки наличия пиццы в объекти типа Element
    private static boolean isPizzaСategory(Element item) {
        return item.select(".business-full-items-grouped-view__title")
                .text().
                equals("Пицца");
    }
}