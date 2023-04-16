package com.example.telegrambot.services;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

/**
 * Сервис по созданию кнопок под полем ввода.
 */
public interface ReplyKeyboardMarkupService {

    /**
     * Создаем кнопку "/start" под полем ввода.
     * @return кнопку.
     */
    ReplyKeyboardMarkup createStart();


    /**
     * Создаем клавиатуру.
     * @param sendMessage
     */
    //void setButtons(SendMessage sendMessage);
}
