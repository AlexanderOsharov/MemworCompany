package com.shurik.memwor_24.pizza_planet.mapper;

import com.shurik.memwor_24.pizza_planet.model.Avatar;
import com.shurik.memwor_24.pizza_planet.model.Phone;

import org.json.JSONException;
import org.json.JSONObject;

public class AvatarMapper {

    public static Avatar avatarFromJson(JSONObject jsonObject) {
        Avatar avatar = null;
        Phone phone = null;

        try {
            // "вынимаем" телефон из json
            phone = new Phone(jsonObject
                    .getString("phone"));

            // "вынимаем" аватарку из json
            avatar = new Avatar(
                    jsonObject.getLong("id"),
                    jsonObject.getString("avatarUri"),
                    phone);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return avatar;
    }

    // получение аватарки из json
    public static Avatar avatarFromUserJson(JSONObject jsonObject) throws JSONException {
        Avatar avatar = null;

        // todo проверить avatarDto
        avatar = avatarFromJson(jsonObject.getJSONObject("avatarDto"));
        return avatar;
    }
}