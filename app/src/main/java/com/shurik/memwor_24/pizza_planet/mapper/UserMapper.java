package com.shurik.memwor_24.pizza_planet.mapper;

import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMapper {

    public static UserEntity userFromJson(JSONObject jsonObject) {
        UserEntity user = null;

        try {
            user = new UserEntity(jsonObject.getLong("userId"), // id - шник
                    jsonObject.getString("userName"), // имя
                    PhoneMapper.phoneFromUserJson(jsonObject), // телефон
                    jsonObject.getString("userMail"), // mail
                    jsonObject.getString("userPassword"), // password
                    AvatarMapper.avatarFromUserJson(jsonObject), // аватарка
                    Integer.parseInt(jsonObject.getString("customerOrNot")) // доставщик или нет
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}