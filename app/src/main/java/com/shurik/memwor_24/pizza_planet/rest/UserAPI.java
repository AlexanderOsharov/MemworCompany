//package com.shurik.memwor_24.pizza_planet.rest;
//
//import com.shurik.memwor_24.pizza_planet.model.Phone;
//import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;
//
//import java.io.IOException;
//
//public interface UserAPI {
//
//    void addUser(UserEntity user);
//    // добавление юзера - при регистрации
//
//    void updateUser(Phone currentPhone, // поиск происходит по номеру телефона
//                    String[] userData,
//                    String[] userDataTitles);
//
//    /**
//     * обновление данных юзера (может быть,
//     * пользователь захочет изменить свои данные)
//     */
//
//    void saveUser(Phone phone); // поиск опр юзера и его сохранение в локальную бд
//
//    boolean checkUserSupplier(UserEntity user);
//
//    /**
//     * пользователь доставщик или нет
//     */
//
//    boolean checkUser(String something,
//                      String whatIsIt) throws IOException;
//    // проверка, что пользователь с something существует или не существует
//}