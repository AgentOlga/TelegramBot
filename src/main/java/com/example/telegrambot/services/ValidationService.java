package com.example.telegrambot.services;

/**
 * Сервис валидации.
 */
public interface ValidationService {

    /**
     * Валидация сущностей.
     * @param object сущность.
     * @return валидная сущность.
     */
    boolean validate(Object object);
}
