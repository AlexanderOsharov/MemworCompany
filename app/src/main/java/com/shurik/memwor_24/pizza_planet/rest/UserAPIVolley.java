package com.shurik.memwor_24.pizza_planet.rest;

import android.content.Context;

import com.shurik.memwor_24.pizza_planet.model.Avatar;
import com.shurik.memwor_24.pizza_planet.model.Phone;
import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;

public class UserAPIVolley implements UserAPI {

    private final Context context;

    public static final String BASE_URL = "http://192.168.56.1:8090"; // говорим, где находится наш сервер

    public UserAPIVolley(Context context) {
        this.context = context;
    }

    // добавление юзера
    @Override
    public void addUser(UserEntity user) {

    }

    // обновление данных юзера
    @Override
    public void updateUser(int id, String newName, Phone newPhone, String newMail, String newPassword, Avatar newAvatar) {

    }

    // проверка данных пользователя
    @Override
    public boolean checkUserData(Phone phone) {
        return false;
    }
}
