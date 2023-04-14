package com.shurik.pizzaplanet.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shurik.pizzaplanet.R;
import com.shurik.pizzaplanet.adapters.ReglogAdapter;
import com.shurik.pizzaplanet.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private ReglogAdapter adapter;
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);

        adapter = new ReglogAdapter(getActivity().getSupportFragmentManager());
        binding.pager.setAdapter(adapter);
        binding.tabs.setupWithViewPager(binding.pager);

        return binding.getRoot();
    }

}