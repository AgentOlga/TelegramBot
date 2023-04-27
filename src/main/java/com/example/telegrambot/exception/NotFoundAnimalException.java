package com.example.telegrambot.exception;

/**
 * Ошибка при отсутствии животного у усыновителя.
 */
public class NotFoundAnimalException extends RuntimeException {
    public NotFoundAnimalException(String message) {
        super("У Вас нет животного. Обратитесь к волонтеру.");
    }
}