package com.shurik.memwor_24.pizza_planet.rest;

import com.shurik.memwor_24.pizza_planet.model.Avatar;
import com.shurik.memwor_24.pizza_planet.model.Phone;
import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;

public interface UserAPI {

    void addUser(UserEntity user); // добавление юзера - при регистрации

    void updateUser(int id,
                    String newName,
                    Phone newPhone,
                    String newMail,
                    String newPassword,
                    Avatar newAvatar);
    /**
     * обновление данных юзера (может быть,
     * пользователь захочет изменить свои данные)
     * <p>
     * если мы хотим поменять только имя, то в остальных передаем null
     * и воспринимаем только те поля, которые не null
     */

    boolean checkUserData(Phone phone);
    /**
     * проверка данных пользователя (ищем по телефону)
     */
}