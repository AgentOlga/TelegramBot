package com.example.telegrambot.exception;

/**
 * Ошибка по поиску усыновителя в БД.
 */
public class NotFoundAdopterException extends RuntimeException{

    public NotFoundAdopterException(String message) {
        super("Ошибка валидации!");
    }
}
