package com.shurik.memwor_24.pizza_planet.mapper;

import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMapper {

    public static UserEntity userFromJson(JSONObject jsonObject) {
        UserEntity user = null;

        try {
            user = new UserEntity(jsonObject.getLong("id"), // id - шник
                    jsonObject.getString("name"), // имя
                    PhoneMapper.phoneFromUserJson(jsonObject), // телефон
                    jsonObject.getString("mail"), // mail
                    jsonObject.getString("password"), // password
                    AvatarMapper.avatarFromUserJson(jsonObject) // аватарка
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}