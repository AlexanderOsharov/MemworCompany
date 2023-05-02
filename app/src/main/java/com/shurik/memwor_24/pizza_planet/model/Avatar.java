package com.shurik.memwor_24.pizza_planet.model;

// класс аватраки пользователя
public class Avatar {

    private long avatarId; // id - шник

    private String avatarUri; // uri картинки

    private Phone phone; // телефон пользователя

    public Avatar(long avatarId, String avatarUri, Phone phone) {
        this.avatarId = avatarId;
        this.avatarUri = avatarUri;
        this.phone = phone;
    }

    public Avatar(String avatarUri, Phone phone) {
        this.avatarUri = avatarUri;
        this.phone = phone;
    }

    public long getId() {
        return avatarId;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public Phone getPhone() {
        return phone;
    }
}