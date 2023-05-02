package com.shurik.memwor_24.pizza_planet.order_database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.shurik.memwor_24.pizza_planet.model.PizzaSev;
import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(tableName = "order")
public class OrderEntity {
    // класс заказа

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "customer")
    @NotNull
    private UserEntity customer; // заказчик

    @ColumnInfo(name = "customerAddress")
    @NotNull
    private String customerAddress;

    @ColumnInfo(name = "pizzaList")
    @NotNull
    private List<PizzaSev> pizzaList;

    @ColumnInfo(name = "completed")
    private int completed = 0;

    public OrderEntity(long id,
                       @NotNull UserEntity customer,
                       @NotNull String customerAddress,
                       @NotNull List<PizzaSev> pizzaList,
                       int completed) {
        this.id = id;
        this.customer = customer;
        this.customerAddress = customerAddress;
        this.pizzaList = pizzaList;
        this.completed = completed;
    }

    public OrderEntity(@NotNull UserEntity customer,
                       @NotNull String customerAddress,
                       @NotNull List<PizzaSev> pizzaList,
                       int completed) {
        this.customer = customer;
        this.customerAddress = customerAddress;
        this.pizzaList = pizzaList;
        this.completed = completed;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomer(@NotNull UserEntity customer) {
        this.customer = customer;
    }

    public void setCustomerAddress(@NotNull String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setPizzaList(@NotNull List<PizzaSev> pizzaList) {
        this.pizzaList = pizzaList;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    @NotNull
    public UserEntity getCustomer() {
        return customer;
    }

    @NotNull
    public String getCustomerAddress() {
        return customerAddress;
    }

    @NotNull
    public List<PizzaSev> getPizzaList() {
        return pizzaList;
    }

    public int getCompleted() {
        return completed;
    }
}