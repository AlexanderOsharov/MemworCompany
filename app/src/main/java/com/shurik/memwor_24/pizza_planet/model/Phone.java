package com.shurik.memwor_24.pizza_planet.model;

// класс телефона пользователя
public class Phone {

    private long id; // id - шник

    private String number; // номер телфона

    public Phone(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    // получение телефона в виде числа
    public static int cleanPhone(Phone phone) {
        int phoneInt;

        String phoneStr = phone.getNumber();

        StringBuilder builder = new StringBuilder("");

        for (int i = 0; i < phoneStr.length(); i++) {
            builder.append(phoneStr.charAt(i));
        }

        phoneInt = Integer.parseInt(builder.toString());
        return phoneInt;
    }
}