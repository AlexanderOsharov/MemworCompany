package com.shurik.memwor_24.pizza_planet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pizza_planet.fragments.MapDialogFragment;
import com.shurik.memwor_24.pizza_planet.model.Organization;
import com.yandex.mapkit.geometry.Point;

import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder> {

    private Context context;

    // список организаций
    private List<Organization> organizationList;

    // адаптер для пицц
    private PizzaAdapter pizzaAdapter;

    // координаты местоположения пользователя
    private Double latitude;
    private Double longitude;

    public OrganizationAdapter(Context context,
                               List<Organization> organizationList,
                               Double latitude,
                               Double longitude) {
        this.context = context;
        this.organizationList = organizationList;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_organization, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Organization organization = organizationList.get(position);

        holder.organizationName.setText(organization.getName());
        holder.organizationAddress.setText(organization.getAddress());

        holder.organizationName.setOnClickListener(view -> // при нажатии на название организации
                // выводим список пицц (горионтальный)
        {
            RecyclerView pizzaRecyclerView = ((Activity) context).findViewById(R.id.pizza_recyclerview_customer);
            pizzaRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            pizzaAdapter = new PizzaAdapter(context, organization.getPizzaList());
            pizzaRecyclerView.setAdapter(pizzaAdapter);
        });

        //
        holder.organizationAddress.setOnClickListener(view -> // при нажатии на адресс организации
                // строим путь до организации
        {
            MapDialogFragment mapDialogFragment = MapDialogFragment.newInstance(
                    new Point(latitude, longitude),
                    new Point(Double.parseDouble(organization.getLatitude()),
                            Double.parseDouble(organization.getLongitude()))
            );

            mapDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "map_dialog");
        });
    }

    @Override
    public int getItemCount() {
        return organizationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView organizationName;
        private TextView organizationAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            organizationName = itemView.findViewById(R.id.organization_name);
            organizationAddress = itemView.findViewById(R.id.organization_address);
        }
    }
}