//package com.shurik.memwor_24.pizza_planet.rest;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.shurik.memwor_24.pizza_planet.PizzaPlanetDatabaseApplication;
//import com.shurik.memwor_24.pizza_planet.mapper.OrderMapper;
//import com.shurik.memwor_24.pizza_planet.model.Phone;
//import com.shurik.memwor_24.pizza_planet.order_database.OrderDAO;
//import com.shurik.memwor_24.pizza_planet.order_database.OrderEntity;
//import com.shurik.memwor_24.pizza_planet.other.SnackBarError;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//// todo основательно проверить все методы
//public class OrderAPIVolley implements OrderAPI {
//
//    private final Context context;
//
//    public static final String BASE_URL = "http://192.168.56.1:8090";
//    // говорим, где находится наш сервер
//    // 8090 - порт сервера
//
//    private Response.ErrorListener errorListener;
//
//    // snackbar ошибки
//    private SnackBarError snackBarError = new SnackBarError();
//
//    private OrderDAO orderDAO = PizzaPlanetDatabaseApplication
//            .getOrderDatabase().orderDao();
//
//    // конструктор
//    public OrderAPIVolley(Context context,
//                          View view,
//                          LayoutInflater inflater
//    ) {
//        this.context = context;
//
//        // "слушатель ошибок"
//        errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // ошибка - например, соединение не удалось
//
//                Log.d("API_TEST", error.toString());
//                // логируем - todo проверить
//
//                // todo - выводим alert dialog или snackbar, который сообщает нам об ошибке
//                snackBarError.showResponseErrorSnackbar(view,
//                        inflater);
//            }
//        };
//    }
//
//    // добавление заказа
//    @Override
//    public void addOrder(OrderEntity order) {
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        // очередь запросов
//
//        String url = BASE_URL + "/order";
//
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.d("API_TEST", response);
//                        // логируем - todo проверить
//
//                        orderDAO.save(order); // сохраняем заказ в локальную базу данных
//                    }
//                }, errorListener
//        ) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                // этот метод отправляет данные на сервер
//
//                Map<String, String> params = new HashMap<>();
//
//                String completed = "" + order.getCompleted();
//
//                // todo - проверит все строчки
//                params.put("customer", order.getCustomer()
//                        .toString());
//                // закидываем customer - а
//
//                params.put("customerAddress", order.getCustomerAddress());
//                // закидываем customerAddress
//
//                params.put("pizzaList", order.getPizzaList()
//                        .toString());
//                // закидываем pizzaList
//
//                params.put("completed", completed);
//                // закидываем состояние заказа
//
//                return params;
//            }
//        };
//        requestQueue.add(request); // добавляем запрос в очередь
//    }
//
//    // сохранение заказа на телефоне у доставщика
//    @Override
//    public void saveOrderForSupplier(Phone phone) {
//        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов
//
//        String url = BASE_URL + "/order";
//
//        JsonArrayRequest arrayRequest = new JsonArrayRequest(
//                Request.Method.GET, // get запрос
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // положительный ответ
//
//                        try {
//
//                            JSONObject object = response.getJSONObject(Phone.cleanPhone(phone));
//                            // ищем заказ по телефону пользователя
//
//                            OrderEntity order = OrderMapper.orderFromJson(object);
//                            // создаем order
//
//                            Log.d("API_TEST", "get order from server");
//                            // логируем - todo проверить
//
//                            orderDAO.save(order); // сохраняем заказ в локалку
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, errorListener // что то пошло не так
//        );
//
//        requestQueue.add(arrayRequest);
//        /**
//         * как только появится возможность,
//         * в очередь добавится запрос и будет ожидаться ответ от сервера
//         */
//    }
//
//    // изменение состояния заказа
//    @Override
//    public void setOrderExecution(Phone phone, int completedPut) {
//        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов
//
//        // todo проверить вот это
//        String url = BASE_URL + "/order/" + Phone.cleanPhone(phone);
//
//        StringRequest request = new StringRequest(
//                Request.Method.PUT,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("API_TEST", response); // логируем - todo проверить
//                    }
//                }, errorListener
//        ) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//
//                // todo - проверить
//
//                String completed = "" + completedPut;
//
//                // изменяем состояние заказа
//                params.put("completed", completed);
//                return params;
//            }
//        };
//        requestQueue.add(request);
//    }
//
//    // удаляем заказ с сервера
//    @Override
//    public void deleteOrderFromServer(Phone phone, OrderEntity order) {
//        RequestQueue requestQueue = Volley.newRequestQueue(context); // очередь запросов
//
//        // todo проверить вот это
//        String url = BASE_URL + "/order/" + Phone.cleanPhone(phone);
//
//        StringRequest request = new StringRequest(
//                Request.Method.DELETE,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("API_TEST", response);
//                        // логируем - todo проверить
//
//                        orderDAO.delete(order);
//                        // удаляем заказ из базы данных
//
//                    }
//                }, errorListener
//        );
//        requestQueue.add(request);
//    }
//}