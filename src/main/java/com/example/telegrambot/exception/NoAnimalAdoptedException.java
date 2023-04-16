package com.example.telegrambot.exception;

public class NoAnimalAdoptedException extends RuntimeException {
    public NoAnimalAdoptedException() {
        super("У Вас нет животного. Обратитесь к волонтеру.");
    }
    public NoAnimalAdoptedException(String message) {
        super(message);
    }

    public NoAnimalAdoptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAnimalAdoptedException(Throwable cause) {
        super(cause);
    }

    protected NoAnimalAdoptedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}