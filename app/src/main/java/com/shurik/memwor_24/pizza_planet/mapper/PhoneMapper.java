package com.shurik.memwor_24.pizza_planet.mapper;

import com.shurik.memwor_24.pizza_planet.model.Phone;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneMapper {

    public static Phone phoneFromJson(JSONObject jsonObject) {
        Phone phone = null;

        try {
            phone = new Phone(
                    jsonObject.getLong("phoneId"),
                    jsonObject.getString("phoneNumber")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return phone;
    }

    // получение телефона из json
    public static Phone phoneFromUserJson(JSONObject jsonObject) throws JSONException {
        Phone phone = null;

        // todo phone phoneDto
        phone = phoneFromUserJson(jsonObject.getJSONObject("phoneDto"));
        return phone;
    }
}