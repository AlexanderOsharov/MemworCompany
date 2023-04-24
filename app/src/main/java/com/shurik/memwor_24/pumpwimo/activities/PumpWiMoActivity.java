package com.shurik.memwor_24.pumpwimo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.shurik.memwor_24.BaseActivity;
import com.shurik.memwor_24.R;
import com.shurik.memwor_24.databinding.PumpwimoMainBinding;
import com.shurik.memwor_24.pumpwimo.adapters.ViewPagerAdapter;

public class PumpWiMoActivity extends BaseActivity {

    PumpwimoMainBinding binding;

    TextView[] dots;

    ViewPagerAdapter viewPagerAdapter;

    public static int whatIsIt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PumpwimoMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.back.setVisibility(View.INVISIBLE);
        binding.back.setOnClickListener(v -> {
            if (getItem(0) > 0) {
                binding.slideViewPager.setCurrentItem(getItem(-1), true);
            }
        });

        binding.next.setOnClickListener(v -> {
            if (getItem(0) < 4) {
                binding.slideViewPager.setCurrentItem(getItem(1), true);
            } else {
                Intent intent = new Intent(PumpWiMoActivity.this, Activity_Intermediate.class);
                whatIsIt = 2;
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        binding.skip.setOnClickListener(v -> {
            Intent intent = new Intent(PumpWiMoActivity.this, Activity_Intermediate.class);

            if (getItem(0) == 4) {
                whatIsIt = 2;
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if (getItem(0) < 4) {
                whatIsIt = 1;
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        viewPagerAdapter = new ViewPagerAdapter(this);

        binding.slideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);
        // что с ней не так?
        binding.slideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpIndicator(int position) {
        dots = new TextView[5];
        binding.indicatorLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorBlack, getApplicationContext().getTheme()));
            binding.indicatorLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.main_3, getApplicationContext().getTheme()));
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

    // 1.Почему мы прибавляем i?
    private int getItem(int i) {
        return binding.slideViewPager.getCurrentItem() + i;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public int getLayoutId() {
        return R.layout.pumpwimo_main;
    }
}
