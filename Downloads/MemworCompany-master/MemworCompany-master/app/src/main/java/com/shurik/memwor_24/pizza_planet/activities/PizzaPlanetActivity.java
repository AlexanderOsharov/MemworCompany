package com.shurik.memwor_24.pizza_planet.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shurik.memwor_24.R;
import com.shurik.memwor_24.databinding.PizzaplanetMainBinding;
import com.shurik.memwor_24.pizza_planet.fragments.BasketFragment;
import com.shurik.memwor_24.pizza_planet.fragments.CustomerFragment;
import com.shurik.memwor_24.pizza_planet.fragments.SupplierFragment;
import com.shurik.memwor_24.pizza_planet.fragments.UserFragment;
import com.shurik.memwor_24.pizza_planet.fragments.UserSettingsFragment;
import com.shurik.memwor_24.pizza_planet.geoLocation.GeoLocation;
import com.shurik.memwor_24.pizza_planet.other.Constants;
import com.yandex.mapkit.MapKitFactory;

import java.util.Arrays;

public class PizzaPlanetActivity extends AppCompatActivity {

    private PizzaplanetMainBinding binding;

    // обьявление и инициализация всех фрагментов (заранее)
    private CustomerFragment customerFragment = new CustomerFragment();
    private SupplierFragment supplierFragment = new SupplierFragment();
    private BasketFragment basketFragment = new BasketFragment();
    private UserFragment userFragment = new UserFragment();
    private UserSettingsFragment userSettingsFragment = new UserSettingsFragment();

    private BottomNavigationView bottomNavigationView; // панелька навигации
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    @SuppressLint({"NonConstantResourceId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PizzaplanetMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recognizeLocation(this);
        mapKitInitialize(this);

        // Добавление всех фрагментов в транзакцию первоначального добавления
        loadInitialFragments(customerFragment, supplierFragment,
                basketFragment, userSettingsFragment, userFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switchFragments(item.getItemId(),
                    customerFragment, supplierFragment,
                    basketFragment, userFragment, userSettingsFragment);
            return true;
        });
    }

    // метод для загрузки фрагментов
    private void loadInitialFragments(Fragment... fragments) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment fragment : fragments) {
            transaction.add(R.id.fragment_container, fragment);
            transaction.hide(fragment);
        }

        transaction.show(fragments[0]); // Показываем первый фрагмент
        transaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    private void switchFragments(int menuItemId,
                                 CustomerFragment customerFragment,
                                 SupplierFragment supplierFragment,
                                 BasketFragment basketFragment,
                                 UserFragment userFragment,
                                 UserSettingsFragment userSettingsFragment) {
        // todo 3.лишний код
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Сначала скрываем все фрагменты
        transaction.hide(customerFragment);
        transaction.hide(supplierFragment);
        transaction.hide(basketFragment);
        transaction.hide(userFragment);
        transaction.hide(userSettingsFragment);

        // Затем показываем тот, который соответствует выбранному элементу меню
        switch (menuItemId) {
            case R.id.nav_supplier_fragment:
                if (SupplierFragment.isSupplier) {
                    transaction.show(supplierFragment);
                } else {
                    SecureAccess();
                    // Выходим из метода, чтобы не продолжать отображение фрагмента
                    return;
                }
                break;
            case R.id.nav_basket_fragment:
                if (BasketFragment.basketAdapter.getItemCount() > 0) {
                    transaction.show(basketFragment);
                } else {
                    EmptyBasket();
                    // Выходим из метода, чтобы не продолжать отображение фрагмента
                    return;
                }
                break;
            case R.id.user_fragment:
                transaction.show(userFragment);
                break;
            case R.id.nav_user_settings_fragment:
                transaction.show(userSettingsFragment);
                break;
            default:
                transaction.show(customerFragment);
                break;
        }
        transaction.commit();
    }

    // инициализация MapKit
    private void mapKitInitialize(Context context) {
        MapKitFactory.setApiKey(Constants.YANDEX_MapKitSDK);
        MapKitFactory.initialize(context);
    }

    // проверка на permission получения данных о текущем местоположении
    private boolean allPermissionsGrantedLocation() {
        return ContextCompat.checkSelfPermission(
                this,
                Arrays.toString(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION})
        ) == PackageManager.PERMISSION_GRANTED;
    }

    // получение данных о местоположении пользователя
    private void recognizeLocation(Context context) {
        if (allPermissionsGrantedLocation()) {
            // permissions предоставлены
            // todo получаем данные
            getLocation(context);
        } else {
            // если permissions не предоставлены - запрашиваем - "вылетает" окошко
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (allPermissionsGrantedLocation()) {
                /**
                 * если после запроса на permissions они предоставлены,
                 * то мы выясняем местоположение
                 */
                recognizeLocation(this);
            }
        }
    }

    // определяем геолокацию пользователя
    private void getLocation(Context context) {
        GeoLocation geolocation = new GeoLocation(context);
        Location location = geolocation.getUserLocation();
    }

    private void SecureAccess() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.secure_access);

        ImageButton closeDetails = dialog.findViewById(R.id.close_button);

        // удаление окошка
        closeDetails.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void EmptyBasket() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.empty_basket);

        ImageButton closeDetails = dialog.findViewById(R.id.close_button);

        // удаление окошка
        closeDetails.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // нажатие на кнопочку back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (TutorialActivity.whatIsIt == 1) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (TutorialActivity.whatIsIt == 2) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}