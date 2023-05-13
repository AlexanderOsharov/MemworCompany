//package com.shurik.memwor_24.pizza_planet.order_database;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import com.shurik.memwor_24.pizza_planet.model.PizzaSev;
//import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//@Entity(tableName = "order")
//// класс заказа
//public class OrderEntity {
//
//    @PrimaryKey(autoGenerate = true)
//    public long orderId; // id - шник
//
//    @ColumnInfo(name = "customer")
//    @NotNull
//    private UserEntity customer; // заказчик
//
//    @ColumnInfo(name = "customerAddress")
//    @NotNull
//    private String customerAddress; // адресс заказчика
//
//    @ColumnInfo(name = "pizzaList")
//    @NotNull
//    private List<PizzaSev> pizzaList; // список пицц
//
//    @ColumnInfo(name = "completed")
//    private int completed = 0; // выполнен, нет, в процессе
//
//    public OrderEntity(long orderId,
//                       @NotNull UserEntity customer,
//                       @NotNull String customerAddress,
//                       @NotNull List<PizzaSev> pizzaList,
//                       int completed) {
//        this.orderId = orderId;
//        this.customer = customer;
//        this.customerAddress = customerAddress;
//        this.pizzaList = pizzaList;
//        this.completed = completed;
//    }
//
//    public OrderEntity(@NotNull UserEntity customer,
//                       @NotNull String customerAddress,
//                       @NotNull List<PizzaSev> pizzaList,
//                       int completed) {
//        this.customer = customer;
//        this.customerAddress = customerAddress;
//        this.pizzaList = pizzaList;
//        this.completed = completed;
//    }
//
//    public void setOrderId(long orderId) {
//        this.orderId = orderId;
//    }
//
//    public void setCustomer(@NotNull UserEntity customer) {
//        this.customer = customer;
//    }
//
//    public void setCustomerAddress(@NotNull String customerAddress) {
//        this.customerAddress = customerAddress;
//    }
//
//    public void setPizzaList(@NotNull List<PizzaSev> pizzaList) {
//        this.pizzaList = pizzaList;
//    }
//
//    public void setCompleted(int completed) {
//        this.completed = completed;
//    }
//
//    public long getOrderId() {
//        return orderId;
//    }
//
//    @NotNull
//    public UserEntity getCustomer() {
//        return customer;
//    }
//
//    @NotNull
//    public String getCustomerAddress() {
//        return customerAddress;
//    }
//
//    @NotNull
//    public List<PizzaSev> getPizzaList() {
//        return pizzaList;
//    }
//
//    public int getCompleted() {
//        return completed;
//    }
//}