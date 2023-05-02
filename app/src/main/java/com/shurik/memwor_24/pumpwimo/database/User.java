package com.shurik.memwor_24.pumpwimo.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "users")
public class User { // класс нашего пользователя

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Email")
    @NotNull
    public String email;

    @ColumnInfo(name = "Password")
    @NotNull
    public String password;

    @ColumnInfo(name = "NickName")
    @NotNull
    public String nickName;

    @ColumnInfo(name = "Phone")
    @NotNull
    public String phone;

    public User(@NonNull String email,
                @NonNull String password,
                @NonNull String phone,
                @NonNull String nickName) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.nickName = nickName;
    }
}