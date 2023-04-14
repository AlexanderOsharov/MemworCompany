package com.shurik.pizzaplanet.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shurik.pizzaplanet.R;
import com.shurik.pizzaplanet.adapters.NewsAdapter;
import com.shurik.pizzaplanet.databinding.FragmentCustomerBinding;
import com.shurik.pizzaplanet.model.New;
import com.shurik.pizzaplanet.model.Organization;
import com.shurik.pizzaplanet.adapters.OrganizationAdapter;
import com.shurik.pizzaplanet.model.Pizza;

import java.util.ArrayList;
import java.util.List;

// фрашмент заказчика
public class CustomerFragment extends Fragment {

    // binding
    private FragmentCustomerBinding binding;

    // recylerView для организаций
    private RecyclerView recyclerViewOrganization;

    // адаптер для организаций
    private OrganizationAdapter organizationAdapter;

    // список организаций
    private ArrayList<Organization> organizationList = new ArrayList<>();

    // список новостей
    private final ArrayList<New> news = new ArrayList<>();

    // адаптер для новостей
    private NewsAdapter newsAdapter;

    // layoutManager
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // инициализация binding - а
        binding = FragmentCustomerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // напполняем список новостей
        setInitialData();

        // инициализируем адаптер
        newsAdapter = new NewsAdapter(news);

        // ставим адаптер viewPager
        binding.viewPager2.setAdapter(newsAdapter);

        // устанавливаем layoutManager recyclerViewOrganization
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOrganization = binding.organizationRecyclerview;
        recyclerViewOrganization.setLayoutManager(layoutManager);

        // возвращаем представление фрагмента (view)
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // список пицц первой оргнизации
        ArrayList<Pizza> pizzaArrayList_1 = new ArrayList<>();

        // TODO Создавать организации, которые находятся рядом с тобой
        // создание организации
        Organization organization_1 = new Organization("Шикарная ул., дом 9, Москва",
                "PizzaPlanet", "1", "", pizzaArrayList_1);

        // добавление пицц
        // TODO Добавление пицц из конкретных ресторанов - сделать!
        pizzaArrayList_1.add(new Pizza("Маргаритта",
                "сыр с помидорчиками",
                "https://e0.edimdoma.ru/data/posts/0002/1429/21429-ed4_wide.jpg?1631194036",
                "360"));
        pizzaArrayList_1.add(new Pizza("Боржоми",
                "сыр с огурчиками",
                "https://www.edimdoma.ru/system/images/contents/0001/3568/wide/85419-original.jpg?1628278420",
                "800"));

        // добавляем организацию_1 в список организаций
        organizationList.add(organization_1);

        // список пицц второй оргнизации
        ArrayList<Pizza> pizzaArrayList_2 = new ArrayList<>();
        pizzaArrayList_2.add(new Pizza("1",
                "0000 u 1111",
                "https://e0.edimdoma.ru/data/posts/0002/1429/21429-ed4_wide.jpg?1631194036",
                "500"));
        pizzaArrayList_2.add(new Pizza("2",
                "0110",
                "https://www.edimdoma.ru/system/images/contents/0001/3568/wide/85419-original.jpg?1628278420",
                "200"));

        // сооздаем вторую организацию
        Organization organization_2 = new Organization("Замечательная ул., дом 13, Москва",
                "PizzaPlanet",
                "1",
                "",
                pizzaArrayList_2);

        // добавляем вторую организацию в список организаций
        organizationList.add(organization_2);

        updateUIWithPizzaVenues(organizationList);
    }

    private void updateUIWithPizzaVenues(List<Organization> pizzaVenuesList) {
        organizationAdapter = new OrganizationAdapter(getContext(), pizzaVenuesList);
        recyclerViewOrganization.setAdapter(organizationAdapter);
    }

    private void setInitialData() {
        /**
         * Здес мы напполняем список новостей
         */
        news.add(new New(
                "2 пиццы",
                R.drawable.pizza_ham,
                R.drawable.pizza_icon_1,
                "Скидка 30%"
        ));
        news.add(new New(
                "Мяснйо комбо",
                R.drawable.pizza_ham,
                R.drawable.pizza_icon_2,
                "Напиток в подарок!"));
        news.add(new New(
                "2 пиццы",
                R.drawable.pizza_ham,
                R.drawable.pizza_icon_3,
                " Скидка 30%"));
        news.add(new New(
                "2 пиццы",
                R.drawable.pizza_ham,
                R.drawable.pizza_icon_4,
                " Скидка 30%"));
        news.add(new New(
                "2 пиццы",
                R.drawable.pizza_ham,
                R.drawable.pizza_icon_5,
                ""));
    }
}