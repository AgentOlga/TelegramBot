package com.example.telegrambot.services;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * Сервис по созданию привязанных кнопок под сообщениями.
 */
public interface InlineKeyboardMarkupService {

    InlineKeyboardMarkup createButtonsShelterTypeSelect();
}
