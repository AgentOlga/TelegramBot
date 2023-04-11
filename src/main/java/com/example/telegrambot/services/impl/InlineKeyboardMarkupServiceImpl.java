package com.example.telegrambot.services.impl;

import com.example.telegrambot.services.InlineKeyboardMarkupService;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;

import static com.example.telegrambot.constants.ConstantValue.*;

/**
 * Бизнес-логика по созданию привязанных кнопок под сообщениями.
 */
@Service
public class InlineKeyboardMarkupServiceImpl implements InlineKeyboardMarkupService {
    @Override
    public InlineKeyboardMarkup createButtonsShelterTypeSelect() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAT_SHELTER)
                .callbackData("button_Cat_Shelter_clicked"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOG_SHELTER)
                .callbackData("button_Dog_Shelter_clicked"));
        return inlineKeyboardMarkup;

    }
}
