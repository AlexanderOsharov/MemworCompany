package com.shurik.pizzaplanet.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Pizza {

    // название пиццы
    private String title;

    // описание
    private String desciption;

    // изображение
    private String pic;

    // цена
    private String fee;

    public Pizza(String title, String desciption, String pic, String fee) {
        this.title = title;
        this.desciption = desciption;
        this.pic = pic;
        this.fee = fee;
    }

    //    public Pizza(String html){
//        Document doc = Jsoup.parse(html);
//        Element pizzaElem = doc.selectFirst("div.business-full-items-grouped-view__title");
//
//        if (pizzaElem != null) {
//            String text = pizzaElem.text();
//            if (text.contains("Пицца")) {
//                Elements categoryElems = pizzaElem.nextElementSiblings();
//                for (Element categoryElem : categoryElems) {
//                    if (categoryElem.hasClass("business-full-items-grouped-view__category")) {
//                        Elements itemElems = categoryElem.nextElementSiblings();
//                        for (Element itemElem : itemElems) {
//                            if (itemElem.hasClass("related-item-list-view__pizzaName")) {
//                                pizzaName.add(itemElem.text());
//                            } else if (itemElem.hasClass("related-item-list-view__price")) {
//                                price.add(itemElem.text());
//                            } else if (itemElem.hasClass("image__bg")) {
//                                Element imgElem = itemElem.selectFirst("img");
//                                if (imgElem != null) {
//                                    imageUrl.add(imgElem.attr("imageUrl"));
//                                }
//                            } else if (itemElem.hasClass("related-item-photo-view__description")) {
//                                pizzaComposition.add(itemElem.attr("pizzaName"));
//                            } else if (itemElem.hasClass("business-full-items-grouped-view__category")) {
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }


    public String getTitle() {
        return title;
    }

    public String getDesciption() {
        return desciption;
    }

    public String getPic() {
        return pic;
    }

    public String getFee() {
        return fee;
    }


}
