package com.shurik.memwor_24.pizza_planet.rest;

import com.shurik.memwor_24.pizza_planet.model.Phone;
import com.shurik.memwor_24.pizza_planet.order_database.OrderEntity;

public interface OrderAPI {

    void addOrder(OrderEntity order);

    /**
     * добавление заказа в базу данных на сервере
     * и в локальную базу данных
     */

    void saveOrderForSupplier(Phone phone);
    /**
     * поиск заказа в базе данных на сервере и
     * сохранение заказа в локальную базу данных на телефоне доставщика
     */

    /**
     * О том как доставщик получает заказ:
     * 1. Он заходит во вкладку - для доставщика
     * 2. Там отображаются ближайшие заказчики
     * 3. Тыкаем на заказачика
     * 4. В базе данных ищем его заказ по телефону, находим его, сохраняем
     * 5. перекидываем доставщика на фрагмент с этим заказом
     */

    void setOrderExecution(Phone phone,
                           int completed);
    // изменение состояния заказа
    /*
     * Почему Phone - ищем заказ в базе данных на сервере по номеру телефона
     */

    void deleteOrderFromServer(Phone phone,
                               OrderEntity order);
    // удаление заказа с сервера и с локалки
}