package com.shurik.pizzaplanet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shurik.pizzaplanet.databinding.FragmentLoginBinding;
import com.shurik.pizzaplanet.other.CurrencyTextWatcher;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // настраиваем editText - ы
        binding.editPhone.addTextChangedListener(new CurrencyTextWatcher());

        // сохраем данные в переменные
        String phone = binding.editPhone.getText().toString();
        String pass = binding.editPass.getText().toString();

        /**
         * проверяем существуют ли данные в бд на сервере
         * Если да - происходит авторизация.
         * Если нет - вылетает утверждение:
         * "Вы не зарегестрированы"
         */
        return binding.getRoot();
    }
}