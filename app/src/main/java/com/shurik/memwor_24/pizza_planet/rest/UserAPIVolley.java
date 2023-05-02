package com.shurik.memwor_24.pizza_planet.rest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shurik.memwor_24.pizza_planet.PizzaPlanetDatabaseApplication;
import com.shurik.memwor_24.pizza_planet.mapper.UserMapper;
import com.shurik.memwor_24.pizza_planet.model.Phone;
import com.shurik.memwor_24.pizza_planet.other.SnackBarError;
import com.shurik.memwor_24.pizza_planet.user_database.UserDAO;
import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class UserAPIVolley implements UserAPI {

    private final Context context;

    public static final String BASE_URL = "http://192.168.56.1:8090"; // говорим, где находится наш сервер

    private Response.ErrorListener errorListener;

    private SnackBarError snackBarError = new SnackBarError();

    private UserDAO userDAO = PizzaPlanetDatabaseApplication
            .getUserDatabase().userDao();

    public UserAPIVolley(Context context,
                         View view,
                         LayoutInflater inflater
    ) {
        this.context = context;

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("API_TEST", error.toString()); // логируем - todo проверить

                // todo - выводим alert dialog или snackbar, который сообщает нам об ошибке
                snackBarError.showResponseErrorSnackbar(view, inflater);
            }
        };
    }

    // добавление юзера
    @Override
    public void addUser(UserEntity user) {
        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов

        String url = BASE_URL + "/user";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API_TEST", response);
                        // логируем - todo проверить

                        userDAO.save(user); // добавляем юзера в локалку
                    }
                }, errorListener
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                String customerOrNot = "" + user.getCustomerOrNot();

                // todo - проверить все строчки
                params.put("userName", user.
                        getUserName());
                // закидываем имя пользователя

                params.put("phone", user.getPhone().
                        getNumber());
                // закидываем телефон

                params.put("userMail", user.
                        getUserMail());
                // закидываем mail

                params.put("userPassword", user.
                        getUserPassword());
                // закидываем password

                params.put("userAvatar", user.getUserAvatar()
                        .toString());
                // закидываем аватарку

                params.put("customerOrNot", customerOrNot);

                return params;
            }
        };
        requestQueue.add(request);
    }

    // обновление юзера
    @Override
    public void updateUser(Phone currentPhone,
                           String[] userData,
                           String[] userDataTitles) {

        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов

        // todo проверить вот это
        String url = BASE_URL + "/user/" + Phone.cleanPhone(currentPhone);

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("API_TEST", response);
                        // логируем - todo проверить
                    }
                }, errorListener
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                for (int i = 0; i < userData.length; i++) {
                    String data = userData[i];
                    String dataTitle = userDataTitles[i];
                    if (Objects.equals(data, "")) continue;
                    else {
                        params.put(dataTitle, data);
                    }
                }
                return params;
            }
        };
        requestQueue.add(request);
    }

    // поиск опр юзера и его сохранение в локальную бд
    @Override
    public void saveUser(Phone phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов

        String url = BASE_URL + "/user";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            JSONObject object = response.
                                    getJSONObject(Phone.cleanPhone(phone));

                            UserEntity user = UserMapper.userFromJson(object);

                            Log.d("API_TEST", "get user from server");
                            // логируем - todo проверить

                            userDAO.save(user);
                            // сохранение пользователя в локальную базу данных

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener
        );

        requestQueue.add(arrayRequest);
    }

    // проверяем, доставщик ли пользователь
    @Override
    public boolean checkUserSupplier(UserEntity user) {
        int customerOrNot = user.getCustomerOrNot();
        if (customerOrNot == 1) return true;
        return false;
    }

    // todo - метод очень сомнительный, его нужно основательно проверить

    // проверка, что пользователь с _something_ существует или не существует
    @Override
    public boolean checkUser(String something,
                             String whatIsIt) throws IOException {
        /**
         * something - проверяемая строка (tropinikita.06@mail.ru, Ivan и т д)
         * whatIsIt - что мы проверем ("userName", "userMail" и т п)
         */

        OkHttpClient client = new OkHttpClient();
        /**
         * OkHttpClient - клиент для HTTP-вызовов,
         * который можно использовать для отправки запросов и чтения ответов
         */

        /**
         * HttpUrl - единый локатор ресурсов (URL) со схемой либо http, либо https.
         * Этот класс используется для составления и
         * декомпозиции интернет-адресов.
         */
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(BASE_URL)
                .newBuilder();

        urlBuilder.addQueryParameter(whatIsIt,
                something);

        String url = BASE_URL + "/user";

        // Класс Request представляет собой HTTP-запрос
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        ;

        okhttp3.Response response = client
                .newCall(request).execute();
        /**
         * Что происходит в этой строчке по шагам:
         * 1. создаем HTTP-ответ;
         * 2. newCall() - метод, который подготавливает запрос - request -
         * к выполнению (в какой-то момент в будущем);
         * 3. execute() - вызывает запрос и блокирует до тех пор,
         * пока ответ не будет обработан или не окажется ошибочным.
         */

        if (response.isSuccessful()) // пользователь с something существует
            return true;
        else // пользователя с something не существует
            return false;
    }
}