package com.shurik.memwor_24.pizza_planet.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.text.TextUtils;
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

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    private List<Pizza> pizzaList;

    public BasketAdapter(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new BasketViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {

        Pizza pizza = pizzaList.get(position);

        holder.pizzaTitle.setText(pizza.getTitle());

        int sum = Integer.parseInt(
                pizza.getFee().replaceAll("\\D+", ""))
                * pizza.getQuantity();

        holder.price.setText(String.valueOf(sum) + pizza.getFee().replaceAll("\\d", ""));
        holder.quantity.setText(String.valueOf(pizza.getQuantity()));

        Glide.with(holder.pizzaImage.getContext())
                .load(pizza.getPic())
                .into(holder.pizzaImage);

        holder.itemView.setOnClickListener(v -> {
            showPizzaDetailsDialog(pizza, holder);
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    class BasketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView pizzaTitle;
        private ImageView pizzaImage;
        private TextView quantity;
        private TextView price;
        private ImageView minus;
        private ImageView plus;
        private ImageButton close;

        public BasketViewHolder(View view) {
            super(view);
            pizzaTitle = view.findViewById(R.id.pizza_title);
            pizzaImage = view.findViewById(R.id.pizza_image);

            quantity = view.findViewById(R.id.quantity);
            price = view.findViewById(R.id.price);

            minus = view.findViewById(R.id.minus);
            minus.setOnClickListener(this);

            plus = view.findViewById(R.id.plus);
            plus.setOnClickListener(this);

            close = view.findViewById(R.id.close);
            close.setOnClickListener(this);
        }

        @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
        @Override
        public void onClick(View v) {
            Pizza item = pizzaList.get(getAdapterPosition());

            switch (v.getId()) {
                case R.id.minus:
                    int qty = 1;
                    if (!TextUtils.isEmpty(quantity.getText().toString())) {
                        qty = Integer.parseInt(quantity.getText().toString());
                    }
                    int sum = 0;
                    if (!TextUtils.isEmpty(price.getText().toString())) {
                        String digitsOnly = price.getText().toString().replaceAll("\\D+", "");
                        sum = Integer.parseInt(digitsOnly);
                    }
                    if (qty > 1) {
                        qty--;
                        String priceString = item.getFee();
                        String digitsOnly = priceString.replaceAll("\\D+", "");
                        sum -= Integer.parseInt(digitsOnly);

                        quantity.setText(String.valueOf(qty));
                        price.setText(String.valueOf(sum) + item.getFee().replaceAll("\\d", ""));
                    }
                    break;

                case R.id.plus:
                    int qty1 = 1;
                    if (!TextUtils.isEmpty(quantity.getText().toString())) {
                        qty1 = Integer.parseInt(quantity.getText().toString());
                    }
                    int sum1 = 0;
                    if (!TextUtils.isEmpty(price.getText().toString())) {
                        String digitsOnly1 = price.getText().toString().replaceAll("\\D+", "");
                        sum1 = Integer.parseInt(digitsOnly1);
                    }
                    qty1++;
                    String priceString1 = item.getFee();
                    String digitsOnly1 = priceString1.replaceAll("\\D+", "");
                    sum1 += Integer.parseInt(digitsOnly1);

                    quantity.setText(String.valueOf(qty1));
                    price.setText(String.valueOf(sum1) + item.getFee().replaceAll("\\d", ""));
                    break;

                case R.id.close:
                    removePizza(getAdapterPosition());
                    break;
            }
        }
    }

    public void addPizza(Pizza pizza) {
        pizzaList.add(pizza);
        notifyItemInserted(pizzaList.indexOf(pizza));
    }

    public void removePizza(int position) {
        // Удалить пиццу из списка и адаптера
        Pizza item = pizzaList.get(position);
        pizzaList.remove(position);
        notifyItemRemoved(position);

        // Удалить пиццу из локальной базы данных
        new Thread(() -> {
            PizzaEntity pizzaEntity = new PizzaEntity();
            pizzaEntity.setId(item.getId());
            // Не забудьте присвоить Id пицце в Pizza.java и установить его при создании Pizza из PizzaEntity
            BasketFragment.pizzaDao.delete(pizzaEntity);
        }).start();
    }

    private void showPizzaDetailsDialog(Pizza pizza, BasketViewHolder holder) {
        final Dialog dialog = new Dialog(holder.itemView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pizza_details);

        TextView pizzaTitleDetails = dialog.findViewById(R.id.titleText);
        ImageView pizzaPicDetails = dialog.findViewById(R.id.picFood);
        TextView pizzaDescriptionDetails = dialog.findViewById(R.id.descriptionTxt);

        TextView pizzaFeeDetails = dialog.findViewById(R.id.feeTxt);

        ImageButton closeDetails = dialog.findViewById(R.id.crest);

        ImageView plus = dialog.findViewById(R.id.plus);
        ImageView minus = dialog.requireViewById(R.id.minus);

        TextView qnt = dialog.findViewById(R.id.numberOrderTxt);

        pizzaTitleDetails.setText(holder.pizzaTitle.getText());

        Glide.with(holder.itemView.getContext())
                .load(pizza.getPic())
                .into(pizzaPicDetails);

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
}