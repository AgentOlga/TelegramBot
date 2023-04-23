package com.example.telegrambot.exception;

/**
 * Ошибка при отсутствии животного у усыновителя.
 */
public class NoAnimalAdoptedException extends RuntimeException {
    public NoAnimalAdoptedException(String message) {
        super("У Вас нет животного. Обратитесь к волонтеру.");
    }
}