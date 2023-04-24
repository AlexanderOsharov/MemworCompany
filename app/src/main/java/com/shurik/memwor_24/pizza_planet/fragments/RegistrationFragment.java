package com.shurik.memwor_24.pizza_planet.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shurik.memwor_24.databinding.FragmentRegistrationBinding;
import com.shurik.memwor_24.pizza_planet.other.CurrencyTextWatcher;

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

