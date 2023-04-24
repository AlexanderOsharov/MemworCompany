package com.shurik.memwor_24.pumpwimo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pumpwimo.adapters.ParamsAdapter;
import com.shurik.memwor_24.pumpwimo.models.Parametr;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    private ArrayList<Parametr> params = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private CardView cardTask1, cardTask2, cardTask3, cardTask4, guide;
    private RecyclerView recyclerView;
    private ImageView menu_1, acc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cardTask1 = findViewById(R.id.cardTask1);
        cardTask2 = findViewById(R.id.cardTask2);
        cardTask3 = findViewById(R.id.cardTask3);
        cardTask4 = findViewById(R.id.cardTask4);
        menu_1 = findViewById(R.id.menu_1);
        acc = findViewById(R.id.acc);
        guide = findViewById(R.id.cardTheGuideBook);
        recyclerView = findViewById(R.id.recyclerview);

        // настраиваем адаптер
        setInitialData();
        ParamsAdapter adapter = new ParamsAdapter(params, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        cardTask1.setOnClickListener(v -> {
            Intent intent = new Intent(BoardActivity.this, QuestActivity.class);
            startActivity(intent);
        });

        cardTask2.setOnClickListener(v -> {
            Intent intent = new Intent(BoardActivity.this, RatingActivity.class);
            startActivity(intent);
        });

        cardTask3.setOnClickListener(v -> {
            Intent intent = new Intent(BoardActivity.this, FriendsActivity.class);
            startActivity(intent);
        });

        cardTask4.setOnClickListener(v -> {
            Intent intent = new Intent(BoardActivity.this, AwardsActivity.class);
            startActivity(intent);
        });

        guide.setOnClickListener(v -> {
            // всплывающее окно
        });

        acc.setOnClickListener(v -> {
            Intent intent = new Intent(BoardActivity.this, ProfileActivity.class);
            startActivity(intent);
            // ккая то анимация
        });

        menu_1.setOnClickListener(v -> {
            // переход нф drawable nav bar
        });
    }

    private void setInitialData() {
        params.add(new Parametr("Статистика", R.drawable.status));
        params.add(new Parametr("Обновления", R.drawable.updated));
        params.add(new Parametr("Premium", R.drawable.premium));
        params.add(new Parametr("Настройки", R.drawable.settings));
    }
}