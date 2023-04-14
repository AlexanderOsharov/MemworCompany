package com.shurik.pizzaplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.shurik.pizzaplanet.databinding.ActivityIntroductoryBinding;

public class IntroductoryActivity extends AppCompatActivity {

    private ActivityIntroductoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityIntroductoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Thread thread = new Thread(new MyThread());
        thread.start();
    }

    // переход на новую активность
    private void transition() {
        Intent intent = new Intent(IntroductoryActivity.this, TutorialActivity.class);
        startActivity(intent);
        // анимация
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(600);
            } catch (InterruptedException ignored) {}
            transition();
        }
    }
}