package com.shurik.memwor_24.pumpwimo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shurik.memwor_24.R;
import com.shurik.memwor_24.databinding.ActivityRegistrationBinding;
import com.shurik.memwor_24.pumpwimo.database.User;
import com.shurik.memwor_24.pumpwimo.database.UserDao;
import com.shurik.memwor_24.pumpwimo.database.UserDatabaseApplication;

import java.util.Objects;

public class  RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    private FirebaseAuth auth; // для авторизации

    private DatabaseReference users; // для работы с табличками внутри бд

    private int permission; // переменная для проверки регитсрации

    private final static String STRING_1 = "Введите данные полностью";
    private final static String STRING_2 = "Учтите требования";

    @SuppressLint("HardwareIds")
    private final String andrId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    private final String password_cor = doUnPassword();

    private UserDao userDao = UserDatabaseApplication.getUserDatabase().userDao();
    @SuppressLint({"ShowToast", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
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
        users = db.getReference("Users");

        // обработка нажатия на кнопку создания аккаунта
        binding.createAcc.setOnClickListener(v -> {
            check(); // 1. проверка требований

            // 2. совпадение - не думаю
            switch (permission) {
                case 1:
                    Snackbar.make(binding.registerLayout, STRING_1, Snackbar.LENGTH_SHORT).show();
                    Log.v("text", STRING_1);
                    break;
                case 2:
                    Snackbar.make(binding.registerLayout, STRING_2, Snackbar.LENGTH_SHORT).show();
                    Log.v("text", STRING_2);
                    break;
                case 3:

                    // Регистрация пользователя
                    assert binding.mailET != null;
                    auth.createUserWithEmailAndPassword(binding.mailET.getText().toString(), password_cor)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                /*
                                обработчик события, который вызовет функцию onSuccess только в том случае,
                                если пользователь будет успешно добавлен в базу данных
                                */
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    User user = new User(
                                            binding.mailET.getText().toString(),
                                            password_cor,
                                            binding.telephoneET.getText().toString(),
                                            binding.nameET.getText().toString()
                                    );
                                    userDao.save(user); // сохраняем юзера в бд
                                     /*
                                    добавляем нового пользователя в табличку users,
                                    ключ, по которому мы идентифицируем пользователя - id пользователя
                                    */

                                    users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                /*
                                                обработчик события, который срботает, когда будет успешное добавление пользователя
                                                */
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Intent intent = new Intent(RegistrationActivity.this, BoardActivity.class);
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                    finish();
                                                }
                                            });

                                }
                            }); // создать пользователя с email - ом и паролем
            }
        });
    }

    // check() проверяет введённые данные на соответствие требованиям
    private void check() {
        assert binding.mailET != null;
        if (TextUtils.isEmpty(binding.mailET.getText().toString())
                || TextUtils.isEmpty(binding.passwordET.getText().toString())
                || TextUtils.isEmpty(binding.telephoneET.getText().toString())
                || TextUtils.isEmpty(binding.nameET.getText().toString())) {
            permission = 1; // введите данные полностью
        } else if (!binding.mailET.getText().toString().contains("@") || password_cor.length() < 8) {
            permission = 2; // учтите требования
        } else if (binding.mailET.getText().toString().contains("@")
        && binding.passwordET.getText().toString().length() >= 8) {
            permission = 3; // разрешение
        }

        Log.v("Permission", "permission == " + permission);
        Log.i("Check", "check == end");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // делаем уникальный пароль
    private String doUnPassword() {
        // берем введенный пароль
        assert binding != null;
        String password = binding.passwordET.getText().toString();

        String password_cor; // конечный пароль
        password_cor = password;
        return password_cor;
    }
}