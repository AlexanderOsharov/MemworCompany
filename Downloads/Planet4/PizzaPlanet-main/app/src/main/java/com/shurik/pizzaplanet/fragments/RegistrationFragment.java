package com.shurik.pizzaplanet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shurik.pizzaplanet.databinding.FragmentRegistrationBinding;
import com.shurik.pizzaplanet.other.CurrencyTextWatcher;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;
    private Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        binding.editPhone.addTextChangedListener(new CurrencyTextWatcher());
        /**
         * Взаимодейтсвие с бд
         * ........
         */
        return binding.getRoot();
    }
}

