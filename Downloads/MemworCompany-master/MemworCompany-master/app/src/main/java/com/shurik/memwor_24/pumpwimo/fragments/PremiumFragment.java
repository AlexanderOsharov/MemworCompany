package com.shurik.memwor_24.pumpwimo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shurik.memwor_24.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PremiumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PremiumFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_premium, container, false);
    }
}