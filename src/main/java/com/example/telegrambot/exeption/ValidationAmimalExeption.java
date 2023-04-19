package com.example.telegrambot.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.BAD_REQUEST)
public class ValidationAmimalExeption extends RuntimeException {
    public ValidationAmimalExeption(String entity) {
        super( "Ошибка валидации " + entity);
    }
}
