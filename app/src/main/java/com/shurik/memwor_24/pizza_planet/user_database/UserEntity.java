package com.shurik.memwor_24.pizza_planet.user_database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.shurik.memwor_24.pizza_planet.model.Avatar;
import com.shurik.memwor_24.pizza_planet.model.Phone;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user")
// класс пользователя
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public long userId; // id - шник пользователя

    @ColumnInfo(name = "name")
    @NotNull
    public String userName; // имя пользователя

    @ColumnInfo(name = "phone")
    @NotNull
    public Phone phone; // телефон

    @ColumnInfo(name = "mail")
    @NotNull
    public String userMail; // mail

    @ColumnInfo(name = "password")
    @NotNull
    public String userPassword; // пароль

    @ColumnInfo(name = "avatar")
    @NotNull
    public Avatar userAvatar; // аватарка

    @ColumnInfo(name = "customerOrNot")
    public int customerOrNot; // доставщик или нет

    @ColumnInfo(name = "insideProfile")
    private int insidePofile = 0; // в профиле или нет

    public UserEntity(long userId,
                      @NotNull String userName,
                      @NotNull Phone phone,
                      @NotNull String userMail,
                      @NotNull String userPassword,
                      @NotNull Avatar userAvatar,
                      int customerOrNot) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.userMail = userMail;
        this.userPassword = userPassword;
        this.userAvatar = userAvatar;
        this.customerOrNot = customerOrNot;
    }

    public UserEntity(@NotNull String userName,
                      @NotNull Phone phone,
                      @NotNull String userMail,
                      @NotNull String userPassword,
                      @NotNull Avatar userAvatar,
                      int customerOrNot) {
        this.userName = userName;
        this.phone = phone;
        this.userMail = userMail;
        this.userPassword = userPassword;
        this.userAvatar = userAvatar;
        this.customerOrNot = customerOrNot;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(@NotNull String userName) {
        this.userName = userName;
    }

    public void setPhone(@NotNull Phone phone) {
        this.phone = phone;
    }

    public void setUserMail(@NotNull String userMail) {
        this.userMail = userMail;
    }

    public void setUserPassword(@NotNull String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserAvatar(@NotNull Avatar userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setCustomerOrNot(int customerOrNot) {
        this.customerOrNot = customerOrNot;
    }

    public void setInsidePofile(int insidePofile) {
        this.insidePofile = insidePofile;
    }

    public long getUserId() {
        return userId;
    }

    @NotNull
    public String getUserName() {
        return userName;
    }

    @NotNull
    public Phone getPhone() {
        return phone;
    }

    @NotNull
    public String getUserMail() {
        return userMail;
    }

    @NotNull
    public String getUserPassword() {
        return userPassword;
    }

    @NotNull
    public Avatar getUserAvatar() {
        return userAvatar;
    }

    public int getCustomerOrNot() {
        return customerOrNot;
    }

    public int getInsidePofile() {
        return insidePofile;
    }
}