package com.shurik.memwor_24.pizza_planet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shurik.memwor_24.databinding.FragmentLoginBinding;
import com.shurik.memwor_24.pizza_planet.other.CurrencyTextWatcherEditPhone;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    // достаем fragment manager
    private FragmentManager fm = getActivity().getSupportFragmentManager();

    private FragmentTransaction ft = fm.beginTransaction();

    // List, который будет хранить данные пользователя
    private List<String> userData = new ArrayList<>(2);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // настраиваем editPhone
        binding.editPhone.addTextChangedListener(new CurrencyTextWatcherEditPhone());

        binding.logButton.setOnClickListener(v -> {
            // сохраем данные в переменные
            String phone = binding.editPhone.getText().toString();
            String pass = binding.editMail.getText().toString();

            /**
             * запускаем анимацию в ui потоке,
             * пока она "играет" - достает данные с сервера (уже в другом потоке)
             */

            new Thread(new Runnable() {
                @Override
                public void run() {
                    goToServer();

                }
            }).start();
        });

        binding.register.setOnClickListener(v -> {
            // переход на фрагмент регистрации

        });
        return binding.getRoot();
    }

    // todo метод, который возвращает массив строчек, в котором лежат данные пользователя
    private List<String> goToServer() {

        return userData;
    }
    /**
     * Как это работает?
     * 1.
     * 2.
     * 3.
     * 4. Записываем данные пользователя в массив строчек:
     * индекс 0 - телефон
     * индекс 1 - пароль
     */


    /**
     * todo методы для работы с сервером
     * todo .....
     */


    /**
     * метод, проверяющий введенные пользователем данные на совпадение с
     * данными, которые пришли с сервера
     */
    private boolean compareUserData(String phoneFromServer,
                                    String passwordFromServer) {

        // todo потвторение кода - плохо!!
        // еще раз сохраняем данные в переменные
        String phone = binding.editPhone.getText().toString();
        String pass = binding.editMail.getText().toString();

        if (phone == phoneFromServer &&
                pass == passwordFromServer) return true;
        else return false;
    }

    // метод, который переходит на фрагмент с данными пользователя или вызывает метод wrongData
    private void transitionOrNot(String phoneFromServer,
                                 String passwordFromServer) {

        if (compareUserData(phoneFromServer,
                passwordFromServer)
        ) {
            // todo ставим фрагмент с данными пользователя
            // todo ...
        } else {
            // todo выдаем сообщения
            wrongData();
        }
    }

    // метод "выдает" пользователю сообщения о каких либо неверных данных
    private void wrongData() {

    }
}