package com.shurik.memwor_24.pizza_planet.user_database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.shurik.memwor_24.pizza_planet.model.Avatar;
import com.shurik.memwor_24.pizza_planet.model.Phone;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user")
public class UserEntity {
    // класс пользователя

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    @NotNull
    public String name;

    @ColumnInfo(name = "phone")
    @NotNull
    public Phone phone;

    @ColumnInfo(name = "mail")
    @NotNull
    public String mail;

    @ColumnInfo(name = "password")
    @NotNull
    public String password;

    @ColumnInfo(name = "avatar")
    @NotNull
    public Avatar avatar;

    public UserEntity(long id,
                      @NotNull String name,
                      @NotNull Phone phone,
                      @NotNull String mail,
                      @NotNull String password,
                      @NotNull Avatar avatar) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
        this.avatar = avatar;
    }

    public UserEntity(@NotNull String name,
                      @NotNull Phone phone,
                      @NotNull String mail,
                      @NotNull String password,
                      @NotNull Avatar avatar) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
        this.avatar = avatar;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setPhone(@NotNull Phone phone) {
        this.phone = phone;
    }

    public void setMail(@NotNull String mail) {
        this.mail = mail;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public void setAvatar(@NotNull Avatar avatar) {
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Phone getPhone() {
        return phone;
    }

    @NotNull
    public String getMail() {
        return mail;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public Avatar getAvatar() {
        return avatar;
    }
}