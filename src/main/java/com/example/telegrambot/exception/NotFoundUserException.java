package com.example.telegrambot.exception;

/**
 * Ошибка по поиску пользователя в БД.
 */
public class NotFoundUserException extends RuntimeException{

    public NotFoundUserException(String message) {
        super("Пользователь не найден в БД!");
    }
}
