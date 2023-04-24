package com.shurik.memwor_24.pumpwimo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pumpwimo.models.QuestGroup;

import java.util.List;

public class QuestGroupAdapter extends RecyclerView.Adapter<QuestGroupAdapter.ViewHolder> {

    private final List<QuestGroup> questGroups;

    public QuestGroupAdapter(List<QuestGroup> questGroups) {
        this.questGroups = questGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quest_group_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestGroup questGroup = questGroups.get(position);
        holder.title.setText(questGroup.getTitle());
        holder.description.setText(questGroup.getDescription());
        holder.cardFon.setImageResource(questGroup.getCardFon());
        holder.icon.setImageResource(questGroup.getIcon());
    }

    @Override
    public int getItemCount() {
        return questGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public ImageView cardFon;
        public ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            cardFon = itemView.findViewById(R.id.cardFon);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}