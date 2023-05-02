package com.shurik.memwor_24.pizza_planet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shurik.memwor_24.databinding.FragmentSupplierBinding;

public class SupplierFragment extends Fragment {

    public static boolean isSupplier = false;

    // binding
    private FragmentSupplierBinding binding;

    // recylerView для заказчиков
    private RecyclerView recyclerViewUser;

//    // адаптер для заказчиков
//    private SupplierAdapter userAdapter;

    // layoutManager
    private LinearLayoutManager layoutManager;

    public SupplierFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSupplierBinding.inflate(inflater, container, false);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewUser = binding.userRecyclerview;
        recyclerViewUser.setLayoutManager(layoutManager);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        userAdapter = new SupplierAdapter(getActivity(), new ArrayList<>());
//        recyclerViewUser.setAdapter(userAdapter);

    }
}