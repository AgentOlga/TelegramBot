package com.example.telegrambot.constants;

/**
 * Статус для усыновителей.
 */
public enum UserStatus {

    APPROVE("Подтвержденный"),
    BLOCKED("Заблокированный");

    private final String translationStatus;

    UserStatus(String translationStatus) {
        this.translationStatus = translationStatus;
    }

    public String getTranslationColor() {
        return translationStatus;
    }
}
