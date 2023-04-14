package com.shurik.pizzaplanet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shurik.pizzaplanet.R;
import com.shurik.pizzaplanet.model.Organization;

import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder> {

    private Context context;

    // список организаций
    private List<Organization> organizationList;

    // адаптер для пицц
    private PizzaAdapter pizzaAdapter;

    // manager для списка пицц
    private LinearLayoutManager pizzaManger = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

    public OrganizationAdapter(Context context, List<Organization> organizationList) {
        this.context = context;
        this.organizationList = organizationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_organization, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // создаем организацию
        Organization organization = organizationList.get(position);

        // ставим имя
        holder.organizationName.setText(organization.getName());

        // ставим адресс
        holder.organizationAddress.setText(organization.getAddress());

        // нажимаем на организаацию

        // TODO нарушение SOLID
        // TODO мы работаем с id customer fragment в стороннем классе
        holder.itemView.setOnClickListener(view -> {
            RecyclerView pizzaRecyclerView = ((Activity) context).findViewById(R.id.popular_list);
            pizzaAdapter = new PizzaAdapter(context, organization.getPizzaList());
            pizzaRecyclerView.setAdapter(pizzaAdapter);
            pizzaRecyclerView.setLayoutManager(pizzaManger);
        });
    }

    @Override
    public int getItemCount() {
        return organizationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView organizationName;
        TextView organizationAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            organizationName = itemView.findViewById(R.id.organization_name);
            organizationAddress = itemView.findViewById(R.id.organization_address);
        }
    }
}