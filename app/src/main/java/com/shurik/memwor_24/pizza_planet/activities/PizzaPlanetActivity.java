package com.shurik.memwor_24.pizza_planet.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shurik.memwor_24.BaseActivity;
import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pizza_planet.fragments.BasketFragment;
import com.shurik.memwor_24.pizza_planet.fragments.CustomerFragment;
import com.shurik.memwor_24.pizza_planet.fragments.SupplierFragment;
import com.shurik.memwor_24.pizza_planet.fragments.UserFragment;
import com.shurik.memwor_24.pizza_planet.fragments.UserSettingsFragment;
import com.shurik.memwor_24.pizza_planet.fragments.geolocation.Geolocation;
import com.shurik.memwor_24.pizza_planet.other.Constants;
import com.yandex.mapkit.MapKitFactory;

public class PizzaPlanetActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    @SuppressLint({"NonConstantResourceId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizzaplanet_main);

        // Запрос на разрешение полученя данных о текущем местоположении
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        // Инициализация Yandex-MapKit
        MapKitFactory.setApiKey(Constants.YANDEX_MapKitSDK);
        MapKitFactory.initialize(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Создание всех фрагментов только один раз
        CustomerFragment customerFragment = new CustomerFragment();
        SupplierFragment supplierFragment = new SupplierFragment();
        BasketFragment basketFragment = new BasketFragment();
        UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
        UserFragment userFragment = new UserFragment();

        // Добавление всех фрагментов в транзакцию первоначального добавления
        loadInitialFragments(customerFragment, supplierFragment, basketFragment, userSettingsFragment, userFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switchFragments(item.getItemId(), customerFragment, supplierFragment, basketFragment, userSettingsFragment, userFragment);
            return true;
        });
    }

    private void loadInitialFragments(Fragment... fragments) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment fragment : fragments) {
            transaction.add(R.id.fragment_container, fragment);
            transaction.hide(fragment);
        }

        transaction.show(fragments[0]); // Показать первый фрагмент
        transaction.commit();
    }

    private void switchFragments(int menuItemId, CustomerFragment customerFragment, SupplierFragment supplierFragment, BasketFragment basketFragment, UserSettingsFragment userSettingsFragment, UserFragment userFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Сначала скрываем все фрагменты
        transaction.hide(customerFragment);
        transaction.hide(supplierFragment);
        transaction.hide(basketFragment);
        transaction.hide(userSettingsFragment);
        transaction.hide(userFragment);

        // Затем показываем тот, который соответствует выбранному элементу меню
        switch (menuItemId) {
            case R.id.nav_customer_fragment:
                transaction.show(customerFragment);
                break;
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
            case R.id.nav_user_settings_fragment:
                transaction.show(userSettingsFragment);
                break;
            case R.id.user_fragment:
                transaction.show(userFragment);
                break;
            default:
                transaction.show(customerFragment);
                break;
        }

        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (TutorialActivity.whatIsIt == 1) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (TutorialActivity.whatIsIt == 2) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, можно вызывать метод getUserLocation() из любого другого класса
                Geolocation geolocation = new Geolocation(this);
                Location location = geolocation.getUserLocation();
            }
        }
    }

    private void SecureAccess() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.secure_access);

        ImageButton closeDetails = dialog.findViewById(R.id.close_button);

        // удаление окошка
        closeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void EmptyBasket() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.empty_basket);

        ImageButton closeDetails = dialog.findViewById(R.id.close_button);

        // удаление окошка
        closeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.pizzaplanet_main;
    }
}
