package com.example.telegrambot.services;

import com.pengrad.telegrambot.model.Update;

/**
 * Сервис по работе с запросами пользователей.
 */

public interface UserRequestService {

    /**
     * Отправление приветственного сообщения пользователю.
     *
     * @param update информация от пользователя
     */
    void sendMessageStart(Update update);

    /**
     * Оживление кнопочного интерфейса.
     *
     * @param update информация от пользователя
     */
    void createButtonClick(Update update);
}
