package com.shurik.pizzaplanet.fragments;

import android.icu.util.CurrencyAmount;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shurik.pizzaplanet.R;
import com.shurik.pizzaplanet.adapters.ReglogAdapter;
import com.shurik.pizzaplanet.databinding.FragmentLoginBinding;
import com.shurik.pizzaplanet.databinding.FragmentUserBinding;
import com.shurik.pizzaplanet.other.CurrencyTextWatcher;

import java.util.SplittableRandom;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.editPhone.addTextChangedListener(new CurrencyTextWatcher());

        /**
         * Взаимодейтсвие с бд
         * ........
         */
        return binding.getRoot();
    }
}