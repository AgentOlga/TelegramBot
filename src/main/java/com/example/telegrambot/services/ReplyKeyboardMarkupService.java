package com.example.telegrambot.services;

import com.pengrad.telegrambot.request.SendMessage;

/**
 * Сервис по созданию кнопок под полем ввода.
 */
public interface ReplyKeyboardMarkupService {

    /**
     * Создаем клавиатуру.
     * @param sendMessage
     */
    void setButtons(SendMessage sendMessage);
}
