package com.shurik.pizzaplanet.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.shurik.pizzaplanet.databinding.FragmentGeolocationBinding;


public class GeolocationFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private FragmentGeolocationBinding binding;

    private TextView textView;

    public GeolocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getLocationPermission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGeolocationBinding.inflate(inflater, container, false);
        textView = binding.pizzaPlaces;
        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getUserLocation();
                }
            }
        }
    }


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getUserLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getUserLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastKnownLocation = location;
                            // Отправка запроса к Foursquare на поиск заведений с пиццей
                            searchForPizzaPlaces(location);
                        } else {
                            Toast.makeText(requireActivity(), "Не удалось определить местоположение", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Toast.makeText(requireActivity(), "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void searchForPizzaPlaces(Location location) {
//        SpoonacularAPI spoonacularAPI = new SpoonacularAPI();
//        spoonacularAPI.getNearbyRestaurants(location);
//        OkHttpClient client = new OkHttpClient();
//
//        final String FOURSQUARE_API = "https://api.foursquare.com/v3/places/search";
//        final String CLIENT_API = Constants.FOURSQUARE_API;
//
//        HttpUrl url = HttpUrl.parse(FOURSQUARE_API);
//        if (url == null) {
//            // Если URL неправильный, отображаем сообщение об ошибке и возвращаемся
//            textView.setText("Неправильный API");
//            return;
//        }
//
//        // Собираем строку запроса
//        HttpUrl.Builder urlBuilder = url.newBuilder();
//        urlBuilder.addQueryParameter("query", "coffee");
//        urlBuilder.addQueryParameter("ll", location.getLatitude() + "," + location.getLongitude());
//        urlBuilder.addQueryParameter("open_now", "true");
//        urlBuilder.addQueryParameter("sort", "DISTANCE");
//
//        Request request = new Request.Builder()
//                .url(urlBuilder.build())
//                .header("Accept", "application/json")
//                .header("Authorization", CLIENT_API)
//                .build();
//
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                e.printStackTrace();
//            }
//
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String jsonResponse = response.body().string();
//                    Log.e("Response", jsonResponse);
//                    Log.e("Response: ", response.body().string());
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(jsonResponse);
//                        JSONObject responseJson = jsonObject.getJSONObject("response");
//                        JSONArray venuesArray = responseJson.getJSONArray("venues");
//
//                        StringBuilder placesBuilder = new StringBuilder();
//                        for (int i = 0; i < venuesArray.length(); i++) {
//                            JSONObject venueObject = venuesArray.getJSONObject(i);
//                            String placeName = venueObject.getString("name");
//
//                            // Добавляем имя заведения в список places
//                            placesBuilder.append(placeName).append("\n");
//                       }
//
//                        final String places = placesBuilder.toString();
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (isAdded() && getActivity() != null) {
//                                    textView.setText(places);
//                                }
//                            }
//                        });
//
//                        // Можно использовать places здесь для обновления пользовательского интерфейса
//                        // или выполнения других операций, например вывод элементов списка в ListView, RecyclerView и т.д.
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    // Handle unsuccessful request
//                    textView.setText("Проблемы с запросом");
//                }
//            }
//        });
    }

}