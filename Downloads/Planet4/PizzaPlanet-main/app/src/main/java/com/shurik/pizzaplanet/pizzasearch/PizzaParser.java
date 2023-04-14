package com.shurik.pizzaplanet.pizzasearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class PizzaParser {

    public void getPizzas(String id) throws IOException {
        String urlReviews = "https://yandex.ru/maps/org/" + id + "/menu";
        Document doc = Jsoup.connect(urlReviews).get();
        if (isContainsMenu(doc)) {
            // TODO: Доделать логику получения пиццы
        }
    }

    public static boolean isContainsMenu(Document doc) {
        Elements menu = doc.select("div.carousel__content");
        return menu.select("a").text().contains("Меню");
    }

    public static boolean isContainsMenu(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements menu = doc.select("div.carousel__content");
        return menu.select("a").text().contains("Меню");
    }
//    public Pizza(String html){
//        Document doc = Jsoup.parse(html);
//        Element pizzaElem = doc.selectFirst("div.business-full-items-grouped-view__title:contains(Пицца)");
//
//        if (pizzaElem != null) {
//            Elements categoryElems = pizzaElem.nextElementSiblings();
//            for (Element categoryElem : categoryElems) {
//                if (categoryElem.hasClass("business-full-items-grouped-view__category")) {
//                    Elements itemElems = categoryElem.nextElementSiblings();
//                    for (Element itemElem : itemElems) {
//                        if (itemElem.hasClass("related-item-list-view__pizzaName")) {
//                            pizzaName.add(itemElem.text());
//                        } else if (itemElem.hasClass("related-item-list-view__price")) {
//                            price.add(itemElem.text());
//                        } else if (itemElem.hasClass("image__bg")) {
//                            Element imgElem = itemElem.selectFirst("img");
//                            if (imgElem != null) {
//                                imageUrl.add(imgElem.attr("imageUrl"));
//                            }
//                        } else if (itemElem.hasClass("related-item-photo-view__description")) {
//                            pizzaComposition.add(itemElem.attr("pizzaName"));
//                        } else if (itemElem.hasClass("business-full-items-grouped-view__category")) {
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }
}
