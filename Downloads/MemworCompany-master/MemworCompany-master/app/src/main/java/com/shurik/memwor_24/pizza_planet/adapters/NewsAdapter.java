package com.shurik.memwor_24.pizza_planet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pizza_planet.model.New;

import java.util.List;

// адаптер для новостей
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final List<New> news;

    public NewsAdapter(List<New> news) {
        this.news = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_new, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        New itNew = news.get(position);
        holder.sNew.setText(itNew.getsNew());
        holder.pic.setImageResource(itNew.getPic());
        holder.pizza_icon.setImageResource(itNew.getPizza_icon());
        holder.description.setText(itNew.getDescription());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView sNew;
        public ImageView pic;
        public ImageView pizza_icon;
        public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sNew = itemView.findViewById(R.id.sNew);
            pic = itemView.findViewById(R.id.pic);
            pizza_icon = itemView.findViewById(R.id.pizza_icon);
            description = itemView.findViewById(R.id.description);
        }
    }
}