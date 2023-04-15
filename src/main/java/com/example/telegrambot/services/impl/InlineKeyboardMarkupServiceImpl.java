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
                .callbackData(CLICK_CAT_SHELTER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOG_SHELTER)
                .callbackData(CLICK_DOG_SHELTER));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsCatShelter() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAT_SHELTER_INFO)
                .callbackData(CLICK_CAT_SHELTER_INFO));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_HOW_TO_ADOPT_A_CAT)
                .callbackData(CLICK_HOW_TO_ADOPT_A_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SEND_A_CAT_REPORT)
                .callbackData(CLICK_SEND_A_CAT_REPORT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_A_VOLUNTEER_CAT)
                .callbackData(CLICK_CALL_A_VOLUNTEER_CAT));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsDogShelter() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOG_SHELTER_INFO)
                .callbackData(CLICK_DOG_SHELTER_INFO));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_HOW_TO_ADOPT_A_DOG)
                .callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SEND_A_DOG_REPORT)
                .callbackData(CLICK_SEND_A_DOG_REPORT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_A_VOLUNTEER_DOG)
                .callbackData(CLICK_CALL_A_VOLUNTEER_DOG));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsCatShelterInfo() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAT_SHELTER_OVERVIEW)
                .callbackData(CLICK_CAT_SHELTER_OVERVIEW));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAR_PASS_CAT)
                .callbackData(CLICK_CAR_PASS_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SAFETY_PRECAUTIONS_CAT)
                .callbackData(CLICK_SAFETY_PRECAUTIONS_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_VISIT_CAT)
                .callbackData(CLICK_VISIT_CAT));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsDogShelterInfo() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOG_SHELTER_OVERVIEW)
                .callbackData(CLICK_DOG_SHELTER_OVERVIEW));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAR_PASS_DOG)
                .callbackData(CLICK_CAR_PASS_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SAFETY_PRECAUTIONS_DOG)
                .callbackData(CLICK_SAFETY_PRECAUTIONS_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_VISIT_DOG)
                .callbackData(CLICK_VISIT_DOG));
        return inlineKeyboardMarkup;
    }
}
