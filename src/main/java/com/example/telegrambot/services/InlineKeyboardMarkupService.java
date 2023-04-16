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
     * Создание кнопок для меню "Информация о приюте" КОШАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsCatShelterInfo();

    /**
     * Создание кнопок для меню "Информация о приюте" СОБАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsDogShelterInfo();

    /**
     * Создание кнопок для меню "Как взять животное из приюта" КОШАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsCatShelterTake();

    /**
     * Создание кнопок для меню "Как взять животное из приюта" СОБАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsDogShelterTake();

    /**
     * Создание кнопок для меню "Прислать отчет о питомце" КОШАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsCatShelterReport();

    /**
     * Создание кнопок для меню "Прислать отчет о питомце" СОБАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsDogShelterReport();

    /**
     * Создание кнопок для меню "Обустройство дома для питомца" КОШАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsCatAtHome();

    /**
     * Создание кнопок для меню "Обустройство дома для питомца" СОБАЧЬЕГО приюта.
     *
     * @return клавиатуру.
     */
    InlineKeyboardMarkup createButtonsDogAtHome();
}
