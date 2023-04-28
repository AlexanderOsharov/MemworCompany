package com.shurik.memwor_24.pumpwimo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pumpwimo.models.Parametr;

import java.util.List;

public class ParamsAdapter extends RecyclerView.Adapter<ParamsAdapter.ViewHolder> {

    private List<Parametr> params;
    private Context context;

    public ParamsAdapter(List<Parametr> params, Context context) {
        this.params = params;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parametr_icon, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parametr parametr = params.get(position);

        holder.textView.setText(parametr.getParametrName());
        holder.imageView.setImageResource(parametr.getParametrImage());
    }

    @Override
    public int getItemCount() {
        return params.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
