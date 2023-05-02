package com.shurik.memwor_24.pizza_planet.model;

// класс телефона пользователя
public class Phone {

    private long phoneId; // id - шник

    private String phoneNumber; // номер телфона

    public Phone(long phoneId, String phoneNumber) {
        this.phoneId = phoneId;
        this.phoneNumber = phoneNumber;
    }

    public Phone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return phoneId;
    }

    public String getNumber() {
        return phoneNumber;
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