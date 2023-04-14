//package com.shurik.pizzaplanet;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.shurik.pizzaplanet.helper.ManagmentCart;
//import com.shurik.pizzaplanet.pizzasearch.CartListAdapter;
//import com.shurik.pizzaplanet.pizzasearch.ChangeNumberItemsListener;
//
//public class CartListActivity extends AppCompatActivity {
//    private RecyclerView.Adapter adapter;
//    private RecyclerView recyclerViewList;
//    private ManagmentCart managmentCart;
//    TextView orderSum, deliverySum, taxSum, totalSum, emptyCart;
//    private double tax;
//    private ScrollView scrollView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart_list);
//
//        managmentCart = new ManagmentCart(this);
//
//        initView();
//        initList();
//        CalculateCart();
//    }
//
//    private void initView() {
//        recyclerViewList = findViewById(R.id.cartRecyclerView);
//        orderSum = findViewById(R.id.orderSum);
//        deliverySum = findViewById(R.id.deliverySum);
//        taxSum = findViewById(R.id.taxSum);
//        totalSum = findViewById(R.id.totalSum);
//        emptyCart = findViewById(R.id.emptyCart);
//        scrollView = findViewById(R.id.cartScrollView);
//        recyclerViewList = findViewById(R.id.cartRecyclerView);
//    }
//
//    private void initList() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerViewList.setLayoutManager(linearLayoutManager);
//        adapter = new CartListAdapter(managmentCart.getListCart(), this, new ChangeNumberItemsListener() {
//            @Override
//            public void changed() {
//                CalculateCart();
//            }
//        });
//
//        recyclerViewList.setAdapter(adapter);
//        if (managmentCart.getListCart().isEmpty()) {
//            emptyCart.setVisibility(View.VISIBLE);
//            scrollView.setVisibility(View.GONE);
//        } else {
//            emptyCart.setVisibility(View.GONE);
//            scrollView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void CalculateCart() {
//        double percentTax = 0.02;
//        double delivery = 10;
//
//        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100) / 100;
//        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
//        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;
//
//        orderSum.setText("₽" + itemTotal);
//        taxSum.setText("₽" + tax);
//        deliverySum.setText("₽" + delivery);
//        totalSum.setText("₽" + total);
//    }
//}