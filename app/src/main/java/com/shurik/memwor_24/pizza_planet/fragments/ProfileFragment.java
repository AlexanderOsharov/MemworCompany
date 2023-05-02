package com.shurik.memwor_24.pizza_planet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shurik.memwor_24.databinding.FragmentProfileBinding;

/**
 * фрагмент профиля пользователя -
 * "грубо говоря", фрагмент с данными пользовтеля (их мы тоже будем доставать с сервера)
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}