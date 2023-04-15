package com.example.telegrambot.services;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * Сервис по созданию привязанных кнопок под сообщениями.
 */
public interface InlineKeyboardMarkupService {

    /**
     * Создание кнопок для выбора приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsShelterTypeSelect();

    /**
     * Создание кнопок для меню КОШАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsCatShelter();

    /**
     * Создание кнопок для меню СОБАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsDogShelter();

    /**
     * Создание кнопок для меню информации КОШАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsCatShelterInfo();

    /**
     * Создание кнопок для меню информации СОБАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsDogShelterInfo();
}
