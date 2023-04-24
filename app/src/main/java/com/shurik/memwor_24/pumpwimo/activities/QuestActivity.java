package com.shurik.memwor_24.pumpwimo.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pumpwimo.adapters.QuestGroupAdapter;
import com.shurik.memwor_24.pumpwimo.models.QuestGroup;

import java.util.ArrayList;

public class QuestActivity extends AppCompatActivity {

    private com.shurik.memwor_24.databinding.ActivityQuestBinding binding;
    private final ArrayList<QuestGroup> questGroups = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.shurik.memwor_24.databinding.ActivityQuestBinding binding = com.shurik.memwor_24.databinding.ActivityQuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setInitialData();
        QuestGroupAdapter questGroupAdapter = new QuestGroupAdapter(questGroups);
        binding.viewPager2.setAdapter(questGroupAdapter);
    }

    private void setInitialData() {
        questGroups.add(new QuestGroup(
                getString(R.string.quest_group_title_1),
                getString(R.string.quest_group_description_1),
                R.drawable.quest_group_shape_1,
                R.drawable.purpose));
        questGroups.add(new QuestGroup(
                getString(R.string.quest_group_title_2),
                getString(R.string.quest_group_description_2),
                R.drawable.quest_group_shape_1,
                R.drawable.vear));
        questGroups.add(new QuestGroup(
                getString(R.string.quest_group_title_3),
                getString(R.string.quest_group_description_3),
                R.drawable.quest_group_shape_1,
                R.drawable.resh));
        questGroups.add(new QuestGroup(
                getString(R.string.quest_group_title_4),
                getString(R.string.quest_group_description_4),
                R.drawable.quest_group_shape_1,
                R.drawable.assiduity));
        questGroups.add(new QuestGroup(
                getString(R.string.quest_group_title_5),
                getString(R.string.quest_group_description_5),
                R.drawable.quest_group_shape_1,
                R.drawable.durability));
        questGroups.add(new QuestGroup(
                getString(R.string.quest_group_title_6),
                getString(R.string.quest_group_description_6),
                R.drawable.quest_group_shape_1,
                R.drawable.duh));
    }
}