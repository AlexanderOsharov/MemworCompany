package com.shurik.memwor_24.pizza_planet.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shurik.memwor_24.R;
import com.shurik.memwor_24.pizza_planet.fragments.BasketFragment;
import com.shurik.memwor_24.pizza_planet.model.Pizza;
import com.shurik.memwor_24.pizza_planet.product_database.PizzaEntity;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.ViewHolder> {

    private static List<Pizza> mPizzaVenues;
    private Context mContext;

    public PizzaAdapter(Context context, List<Pizza> pizzaVenues) {
        mContext = context;
        mPizzaVenues = pizzaVenues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pizza_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pizza pizzaVenue = mPizzaVenues.get(position);

        holder.pizzaTitle.setText(pizzaVenue.getTitle());

        loadImageIntoImageView(mContext, pizzaVenue.getPic(), holder.pizzaPic);

        holder.pizzaFee.setText(pizzaVenue.getFee());

        // Действие при нажатии на кнопку или на корзину
        holder.basketButton.setOnClickListener(v -> addItemToBasketAdapter(pizzaVenue));
        holder.basketImage.setOnClickListener(v -> addItemToBasketAdapter(pizzaVenue));

        // Действие при нажатии на картинку
        holder.pizzaPic.setOnClickListener(v -> showPizzaDetailsDialog(pizzaVenue, holder));

        holder.plus.setOnClickListener(v -> {
            int qty1 = Integer.parseInt(holder.quantity.getText().toString());
            qty1++;
            holder.quantity.setText(String.valueOf(qty1));
        });

        holder.minus.setOnClickListener(v -> {
            int qty = Integer.parseInt(holder.quantity.getText().toString());
            if (qty > 1) {
                qty--;
                holder.quantity.setText(String.valueOf(qty));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPizzaVenues.size();
    }

    // viewHolder для пиццы
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pizzaTitle;
        private ImageView pizzaPic;
        private TextView pizzaFee;
        private ImageButton basketButton;
        private ImageView basketImage;
        private ImageView minus;
        private TextView quantity;
        private ImageView plus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaTitle = itemView.findViewById(R.id.title);
            pizzaPic = itemView.findViewById(R.id.pic);
            pizzaFee = itemView.findViewById(R.id.fee);

            basketButton = itemView.findViewById(R.id.addBtn);
            basketImage = itemView.findViewById(R.id.basketBtn);

            minus = itemView.findViewById(R.id.minus);
            quantity = itemView.findViewById(R.id.numberOrderTxt);
            plus = itemView.findViewById(R.id.plus);
        }
    }

    // загружаем картинку
    private void loadImageIntoImageView(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context).load(imageUrl).centerCrop().into(imageView);
    }

    private void showPizzaDetailsDialog(Pizza pizza, ViewHolder holder) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pizza_details);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.pizza_details_shape);

        TextView pizzaTitleDetails = dialog.findViewById(R.id.titleText);
        ImageView pizzaPicDetails = dialog.findViewById(R.id.picFood);

        TextView pizzaDescriptionDetails = dialog.findViewById(R.id.descriptionTxt);
        TextView pizzaFeeDetails = dialog.findViewById(R.id.feeTxt);

        ImageButton closeDetails = dialog.findViewById(R.id.crest);

        ImageView minus = dialog.requireViewById(R.id.minus);
        TextView qnt = dialog.findViewById(R.id.numberOrderTxt);
        ImageView plus = dialog.findViewById(R.id.plus);

        pizzaTitleDetails.setText(holder.pizzaTitle.getText());
        loadImageIntoImageView(mContext, pizza.getPic(), pizzaPicDetails);
        pizzaDescriptionDetails.setText(pizza.getDescription());
        pizzaFeeDetails.setText(pizza.getFee());
        qnt.setText(holder.quantity.getText());

        // удаление окошка
        closeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        plus.setOnClickListener(v -> {
            int qty1 = Integer.parseInt(qnt.getText().toString());
            qty1++;
            qnt.setText(String.valueOf(qty1));
            holder.quantity.setText(String.valueOf(qty1));
            pizza.setQuantity(qty1);
        });

        minus.setOnClickListener(v -> {
            int qty = Integer.parseInt(holder.quantity.getText().toString());
            if (qty > 1) {
                qty--;
                qnt.setText(String.valueOf(qty));
                holder.quantity.setText(String.valueOf(qty));
                pizza.setQuantity(qty);
            }
        });

        dialog.show();
    }

    private void addItemToBasketAdapter(Pizza pizza) {

        // Преобразовываем объект Pizza в PizzaEntity объект
        PizzaEntity pizzaEntity = new PizzaEntity();

        pizzaEntity.setTitle(pizza.getTitle());
        pizzaEntity.setPic(pizza.getPic());
        pizzaEntity.setDesciption(pizza.getDescription());

        pizzaEntity.setFee(pizza.getFee());
        pizzaEntity.setQuantity(pizza.getQuantity());

        // Добавить PizzaEntity в базу данных
        new Thread(() -> BasketFragment.pizzaDao.insert(pizzaEntity)).start();

        // Добавить Pizza в адаптер
        BasketFragment.basketAdapter.addPizza(pizza);
    }
}