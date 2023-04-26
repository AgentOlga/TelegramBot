package com.example.telegrambot.constants;

public enum UserType {

    DEFAULT("Пользователь"),
    GUEST("Гость"),
    ADOPTER("Усыновитель"),
    VOLUNTEER("Волонтер");
    private final String translationType;

    UserType(String translationType) {
        this.translationType = translationType;
    }

    public String getTranslationColor() {
        return translationType;
    }
}
