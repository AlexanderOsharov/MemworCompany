package com.shurik.memwor_24.pumpwimo.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shurik.memwor_24.pumpwimo.database.UserDao;
import com.shurik.memwor_24.pumpwimo.database.UserDatabaseApplication;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth auth; // для авторизации

    private int permission; // переменная для проверки

    private final static String STRING_1 = "Введите данные полностью";
    private final static String STRING_2 = "Учтите требования";

    private UserDao userDao = UserDatabaseApplication.getUserDatabase().userDao();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // теперь мы поместим данные в наши переменные
        auth = FirebaseAuth.getInstance(); // запускаем авторизацию в бд
        // для подключения к базе даннных
        FirebaseDatabase db = FirebaseDatabase.getInstance(); // подключаемся к бд

        /*
        Указываем название таблички, с которой мы будем работать
         */
        // табличка Users - пользователи

        // для работы с табличками внутри бд
        DatabaseReference users = db.getReference("Users");

        binding.guest.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, BoardActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        binding.signInBtn.setOnClickListener(v -> {
            check();

            switch (permission) {
                case 1:
                    Snackbar.make(binding.loginLayout, STRING_1, Snackbar.LENGTH_SHORT).show();
                    Log.v("text", STRING_1);
                    break;
                case 2:
                    Snackbar.make(binding.loginLayout, STRING_2, Snackbar.LENGTH_SHORT).show();
                    Log.v("text", STRING_2);
                    break;
                case 3:
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    // если есть wi - fi вход через firebase
                    if (mWifi.isConnected()) {
                        auth.signInWithEmailAndPassword(binding.usernameET.getText().toString(), binding.passwordET.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() { // успешная авторизация
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        startActivity(new Intent(LoginActivity.this, BoardActivity.class));
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(binding.loginLayout, "Неправильные данные\nВозможно Вы не зарегестрированы", Snackbar.LENGTH_SHORT).show();
                                    }
                                }); //не успешное добавление пользователя
                    } else {
                        // проверяем даннные через бд устройства
                    }
            }
        });

        // создать аккаунт
        binding.signUpBtn.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
        });
    }

    private void check() {
        if (TextUtils.isEmpty(binding.usernameET.getText().toString())
                || TextUtils.isEmpty(binding.passwordET.getText().toString())) {
            permission = 1; // введите данные полностью
        } else if (!binding.usernameET.getText().toString().contains("@") || binding.passwordET.getText().toString().length() <= 8) {
            permission = 2; // учтите требования
        } else if (binding.usernameET.getText().toString().contains("@") && binding.passwordET.getText().toString().length() > 8) {
            permission = 3; // разрешение
        }
        Log.v("Permission", "permission == " + permission);
        Log.i("Check", "check == end");
    }

    // для кнопки back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}