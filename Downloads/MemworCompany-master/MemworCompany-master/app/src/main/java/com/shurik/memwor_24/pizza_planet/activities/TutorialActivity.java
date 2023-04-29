package com.shurik.memwor_24.pizza_planet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.databinding.ActivityTutorialBinding;
import com.shurik.memwor_24.pizza_planet.adapters.ViewPagerAdapter;

public class TutorialActivity extends AppCompatActivity {

    private ActivityTutorialBinding binding;

    private TextView[] dots;

    private ViewPagerAdapter viewPagerAdapter;

    /**
     * переменная, которая хранит 2 значения
     * 1 - если пользователь не находится на последнем слайде tutorial - a
     * 2 - если пользователь находится на последнем слайде tutorial - a
     */
    public static int whatIsIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // кнопочка "Назад"
        binding.back.setVisibility(View.INVISIBLE);
        binding.back.setOnClickListener(v -> {
            if (getItem(0) > 0) {
                binding.slideViewPager.setCurrentItem(getItem(-1), true);
            }
        });

        // кнопочка "Дальше"
        binding.next.setOnClickListener(v -> {
            if (getItem(0) < 3) {
                binding.slideViewPager.setCurrentItem(getItem(1), true);
            } else {
                Intent intent = new Intent(TutorialActivity.this, PizzaPlanetActivity.class);
                whatIsIt = 2;
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // кнопочка "Пропустить"
        binding.skip.setOnClickListener(v -> {
            Intent intent = new Intent(TutorialActivity.this, PizzaPlanetActivity.class);
            if (getItem(0) == 3) {
                whatIsIt = 2;
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (getItem(0) < 3) {
                whatIsIt = 1;
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.slideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);
        binding.slideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpIndicator(int position) {
        dots = new TextView[4];
        binding.indicatorLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white, getApplicationContext().getTheme()));
            binding.indicatorLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.yel_oran, getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpIndicator(position);

            if (position == 0) {
                binding.back.setVisibility(View.INVISIBLE);
            } else if (position >= 1) {
                binding.back.setVisibility(View.VISIBLE);
            } else {
                binding.back.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    // метод дл определения номера слайда, на котором находится пользователь
    private int getItem(int i) {
        return binding.slideViewPager.getCurrentItem() + i;
    }
}

/*
 * todo 1. Сделать плавную анимацию перехода для текста
 */