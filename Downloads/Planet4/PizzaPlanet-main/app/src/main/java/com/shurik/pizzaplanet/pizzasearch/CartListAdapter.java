
//package com.shurik.pizzaplanet.pizzasearch;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.shurik.pizzaplanet.R;
//import com.shurik.pizzaplanet.domain.FoodDomain;
//import com.shurik.pizzaplanet.helper.ManagmentCart;
//
//import java.util.ArrayList;
//
//public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
//    private ArrayList<FoodDomain> foodDomains;
//    private ManagmentCart managmentCart;
//    private ChangeNumberItemsListener changeNumberItemsListener;
//
//    public CartListAdapter(ArrayList<FoodDomain> foodDomains, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
//        this.foodDomains = foodDomains;
//        this.managmentCart = new ManagmentCart(context);
//        this.changeNumberItemsListener = changeNumberItemsListener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
//        return new ViewHolder(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.title.setText(foodDomains.get(position).getTitle());
//        holder.feeEachItem.setText(String.valueOf(foodDomains.get(position).getFee()));
//        holder.totalEachItem.setText(String.valueOf(Math.round((foodDomains.get(position).getNumberInCart() * foodDomains.get(position).getFee()) * 100) / 100));
//        holder.num.setText(String.valueOf(foodDomains.get(position).getNumberInCart()));
//
//        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(foodDomains.get(position).getPic()
//                , "drawable", holder.itemView.getContext().getPackageName());
//        Glide.with(holder.itemView.getContext())
//                .load(drawableResourceId)
//                .into(holder.pic);
//
//        holder.plusItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                managmentCart.plusNumberFood(foodDomains, position, new ChangeNumberItemsListener() {
//                    @Override
//                    public void changed() {
//                        notifyDataSetChanged();
//                        changeNumberItemsListener.changed();
//                    }
//                });
//            }
//        });
//
//        holder.minusItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                managmentCart.minusNumberFood(foodDomains, position, new ChangeNumberItemsListener() {
//                    @Override
//                    public void changed() {
//                        notifyDataSetChanged();
//                        changeNumberItemsListener.changed();
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return foodDomains.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView title, feeEachItem;
//        ImageView pic, plusItem, minusItem;
//        TextView totalEachItem, num;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.cartTitle);
//            feeEachItem = itemView.findViewById(R.id.feeEachItem);
//            pic = itemView.findViewById(R.id.cartPic);
//            totalEachItem = itemView.findViewById(R.id.totalEachItem);
//            num = itemView.findViewById(R.id.numberItem);
//            plusItem = itemView.findViewById(R.id.plusCartBtn);
//            minusItem = itemView.findViewById(R.id.minusCartBtn);
//        }
//    }
//}
