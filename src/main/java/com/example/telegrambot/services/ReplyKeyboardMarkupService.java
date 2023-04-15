package com.example.telegrambot.services;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

/**
 * Сервис по созданию кнопок под полем ввода.
 */
public interface ReplyKeyboardMarkupService {
    ReplyKeyboardMarkup createStart();


    /**
     * Создаем клавиатуру.
     * @param sendMessage
     */
    //void setButtons(SendMessage sendMessage);
}
