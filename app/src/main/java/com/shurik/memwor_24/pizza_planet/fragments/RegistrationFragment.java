package com.shurik.memwor_24.pizza_planet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shurik.memwor_24.databinding.FragmentRegistrationBinding;
import com.shurik.memwor_24.pizza_planet.other.CurrencyTextWatcherEditPhone;

import java.util.ArrayList;
import java.util.List;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    private List<String> userDaat = new ArrayList<>(4);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false);


        binding.editPhoneReg.addTextChangedListener(new CurrencyTextWatcherEditPhone());

        // регистрация
        binding.regButton.setOnClickListener(m -> {
            String name = binding.editNameReg.getText().toString();
            String phone = binding.editPhoneReg.getText().toString();
            String mail = binding.editMailReg.getText().toString();
            String password = binding.editPassReg.getText().toString();

            // todo работа сервером чет еще
        });
        return binding.getRoot();
    }

    // todo метод, который сохраняет данные (закидывает их в бд на сервере)
    private boolean saveData() {
        if (true) {

        } else {
            // todo если неудача - метод
            // todo ....
            if (true) {

            } else {
                return false;
            }
        }
        return false;
    }
    /**
     * Как это работает?
     * 1.
     * 2.
     * 3.
     * 4. Сохранем данные пользователя в соответствующих ячейках в базе данных на сервере
     */


    /**
     * todo методы для работы с сервером
     * todo .....
     */


    // метод, который "переходит" на фрагмент с данными пользователя
    private void transitionOrNot(String phoneFromServer,
                                 String passwordFromServer) {

    }

    // метод, который проверяет данные на соответствие требованиям
    private void checkUserData(List<String> userData) {

        //todo повторение кода - плохо!

        // сохраняем данные в переменные
        String name = binding.editNameReg.getText().toString();
        String phone = binding.editPhoneReg.getText().toString();
        String mail = binding.editMailReg.getText().toString();
        String password = binding.editPassReg.getText().toString();

        if (true) {

        } else {
            int editVals[] = {
              1, 1, 1, 1
            };
            meaasageWrongData(editVals);
        }
    }
    /**
     * метод выдает сообщения о том, что нужно исправить
     * (пользователь не учел какие либо ребования к данным)
     */
    private void meaasageWrongData(int editVals[]) {
        for(int editVal: editVals) {
            if (true) {

            }
        }
    }
}