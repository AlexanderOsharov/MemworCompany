package com.shurik.memwor_24.pizza_planet.rest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.shurik.memwor_24.pizza_planet.PizzaPlanetDatabaseApplication;
import com.shurik.memwor_24.pizza_planet.mapper.OrderMapper;
import com.shurik.memwor_24.pizza_planet.model.Phone;
import com.shurik.memwor_24.pizza_planet.order_database.OrderDAO;
import com.shurik.memwor_24.pizza_planet.order_database.OrderEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderAPIVolley implements OrderAPI {

    private final Context context;

    public static final String BASE_URL = "http://192.168.56.1:8090"; // говорим, где находится наш сервер
    // 8090 - порт сервера

    private Response.ErrorListener errorListener;

    private OrderDAO orderDAO = PizzaPlanetDatabaseApplication.getOrderDatabase().orderDao();;

    private Snackbar snackbar;

    public OrderAPIVolley(Context context) {
        this.context = context;

        // "слушателя ошибок"
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // ошибка - например, соединение не удалось

                Log.d("API_TEST", error.toString()); // логируем - todo проверить

                // todo выводим alert dialog или snackbar

                showResponseErrorSnackbar(context);
            }
        };
    }

    // добавление заказа
    @Override
    public void addOrder(OrderEntity order) {
        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов

        String url = BASE_URL + "/order";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillOrderList();
                        Log.d("API_TEST", response); // логируем - todo проверить
                    }
                }, errorListener
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // этот метод отправляет даннеы на сервер

                Map<String, String> params = new HashMap<>();

                String completed = "" + order.getCompleted();

                // todo какие то строчки
                params.put("customer", order.getCustomer().toString());
                params.put("customerAddress", order.getCustomerAddress().toString());
                params.put("pizzaList", order.getPizzaList().toString());
                params.put("completed", completed);
                return params;
            }
        };
        requestQueue.add(request);
    }

    // получение заказа (для доставщика)
    @Override
    public void getOrder(Phone phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов

        String url = BASE_URL + "/order";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET, // get запрос
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // положительный ответ

                        try {
                            JSONObject object = response.getJSONObject(Phone.cleanPhone(phone)); // ищем заказ по телефону
                            OrderEntity order = OrderMapper.orderFromJson(object); // создаем order

                            Log.d("API_TEST", "get order from server"); // логируем - todo проверить

                            /**
                             * todo достали наш заказ, кидаем его на фрагмент
                             * todo Доставить - оставляем его
                             * todo backPressed - идем обратно на спико близко находящихся
                             */

                            if () {
                                // todo если нажали кнопку - то добавление в бд
                                orderDAO.save(order);
                            } else {
                                // todo все возвращаем на круги своя
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener // что то пошло не так
        );

        requestQueue.add(arrayRequest);
        /**
         * как только у библиотеки появиться возможность,
         * в очередь добавляется запрос и ожидается ответ от сервера
         */
    }

    // заполняем список наших заказов теми, что лежат на сервере
    @Override
    public void fillOrderList() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/order";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET, // get запрос
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                OrderEntity order = OrderMapper.orderFromJson(object);
                                orderDAO.save(order);
                            }

                            Log.d("API_TEST", "get order from server"); // логируем - todo проверить

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, errorListener
        );
        requestQueue.add(arrayRequest);
    }

    // ставим состояние заказа
    @Override
    public void setOrderExecution(OrderEntity order) {

    }

    // удаляем заказ с сервера
    @Override
    public void deleteOrderFromServer(OrderEntity order) {

    }

    // метод для создания snackbar - а ошибки
    private void showResponseErrorSnackbar(Context context) {

    }
}