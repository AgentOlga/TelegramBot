package com.example.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка валидации.
 */
public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super("Ошибка валидации!");
    }
}
