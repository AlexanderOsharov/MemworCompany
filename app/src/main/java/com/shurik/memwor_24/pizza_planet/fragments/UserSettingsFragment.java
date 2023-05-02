package com.shurik.memwor_24.pizza_planet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shurik.memwor_24.databinding.FragmentUserSettingsBinding;

public class UserSettingsFragment extends Fragment {

    private FragmentUserSettingsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentUserSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}