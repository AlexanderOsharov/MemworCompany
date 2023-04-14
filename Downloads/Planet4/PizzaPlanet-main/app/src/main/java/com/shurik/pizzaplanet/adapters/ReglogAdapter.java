package com.shurik.pizzaplanet.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shurik.pizzaplanet.fragments.LoginFragment;
import com.shurik.pizzaplanet.fragments.RegistrationFragment;

// адаптер для фрагментов регистрации и авторизации
public class ReglogAdapter extends FragmentPagerAdapter {

    private final Fragment[] fragments = new Fragment[2];

    public ReglogAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragments[0] = new LoginFragment();
        fragments[1] = new RegistrationFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Авторизация" : "Регистрация";
    }
}
