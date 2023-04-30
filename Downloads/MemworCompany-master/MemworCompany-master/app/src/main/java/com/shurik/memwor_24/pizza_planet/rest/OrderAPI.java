package com.shurik.memwor_24.pizza_planet.rest;

import com.shurik.memwor_24.pizza_planet.model.Phone;
import com.shurik.memwor_24.pizza_planet.order_database.OrderEntity;

public interface OrderAPI {

    void addOrder(OrderEntity order);
    /**
     * добавление заказа в базу данных на сервере
     * и в локальную базу данных
     */

    void getOrder(Phone phone); // получение заказа (для доставщика)

    /**
     * О том как доставщик получает заказ:
     * 1. Он заходит во вкладку - для доставщика
     * 2. Там отображаются ближайшие заказчики
     * 3. Тыкаем на заказачика
     * 4. В базе данных ищем его заказ по телефону, находим его и выдаем доставщику
     */

    void fillOrderList(); // заполняем наш список заказов тем, что есть на сервере

    void setOrderExecution(OrderEntity order); // ставим состояние заказа, кидаем на сервак

    void deleteOrderFromServer(OrderEntity order); // удаление заказа с сервера, если он выполнен
}