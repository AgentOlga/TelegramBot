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
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SHELTER_INFO)
                .callbackData(CLICK_CAT_SHELTER_INFO));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_HOW_TO_ADOPT)
                .callbackData(CLICK_HOW_TO_ADOPT_A_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SEND_REPORT)
                .callbackData(CLICK_SEND_A_CAT_REPORT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_BACK_TO_SHELTER_TYPE));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsDogShelter() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SHELTER_INFO)
                .callbackData(CLICK_DOG_SHELTER_INFO));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_HOW_TO_ADOPT)
                .callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SEND_REPORT)
                .callbackData(CLICK_SEND_A_DOG_REPORT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_BACK_TO_SHELTER_TYPE));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsCatShelterInfo() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SHELTER_OVERVIEW)
                .callbackData(CLICK_CAT_SHELTER_OVERVIEW));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAR_PASS)
                .callbackData(CLICK_CAR_PASS_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SAFETY_PRECAUTIONS)
                .callbackData(CLICK_SAFETY_PRECAUTIONS_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_VISIT)
                .callbackData(CLICK_VISIT_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_CAT_SHELTER));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsDogShelterInfo() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SHELTER_OVERVIEW)
                .callbackData(CLICK_DOG_SHELTER_OVERVIEW));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAR_PASS)
                .callbackData(CLICK_CAR_PASS_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_SAFETY_PRECAUTIONS)
                .callbackData(CLICK_SAFETY_PRECAUTIONS_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_VISIT)
                .callbackData(CLICK_VISIT_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_DOG_SHELTER));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsCatShelterTake() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_GREETING_WITH_PET)
                .callbackData(CLICK_GREETING_WITH_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOC_PET)
                .callbackData(CLICK_DOC_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_TRANSPORT_PET)
                .callbackData(CLICK_TRANSPORT_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_ARRANGEMENT_PET_HOME)
                .callbackData(CLICK_ARRANGEMENT_CAT_HOME));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_REASONS_FOR_REFUSAL)
                .callbackData(CLICK_REASONS_FOR_REFUSAL_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_CAT_SHELTER));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsDogShelterTake() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_GREETING_WITH_PET)
                .callbackData(CLICK_GREETING_WITH_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOC_PET)
                .callbackData(CLICK_DOC_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_TRANSPORT_PET)
                .callbackData(CLICK_TRANSPORT_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_ARRANGEMENT_PET_HOME)
                .callbackData(CLICK_ARRANGEMENT_DOG_HOME));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_REASONS_FOR_REFUSAL)
                .callbackData(CLICK_REASONS_FOR_REFUSAL_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CYNOLOGIST_ADVICE)
                .callbackData(CLICK_CYNOLOGIST_ADVICE));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_FIND_CYNOLOGIST)
                .callbackData(CLICK_FIND_CYNOLOGIST));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_DOG_SHELTER));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsCatShelterReport() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_RULES_REPORT_PET)
                .callbackData(CLICK_RULES_REPORT_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_REPORT_PET)
                .callbackData(CLICK_REPORT_CAT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_CAT_SHELTER));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsDogShelterReport() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_RULES_REPORT_PET)
                .callbackData(CLICK_RULES_REPORT_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_REPORT_PET)
                .callbackData(CLICK_REPORT_DOG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_DOG_SHELTER));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsCatAtHome() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_KITTY)
                .callbackData(CLICK_KITTY));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CAT_BIG)
                .callbackData(CLICK_CAT_BIG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_PET_INVALID)
                .callbackData(CLICK_CAT_INVALID));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_HOW_TO_ADOPT_A_CAT));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsDogAtHome() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_PUPPY)
                .callbackData(CLICK_PUPPY));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_DOG_BIG)
                .callbackData(CLICK_DOG_BIG));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_PET_INVALID)
                .callbackData(CLICK_DOG_INVALID));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CALL_VOLUNTEER)
                .callbackData(CLICK_CALL_A_VOLUNTEER));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_BACK)
                .callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createButtonsVolunteerMenu() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();



        //todo остальные кнопки меню


        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_CHECK_REPORT)
                .callbackData(CLICK_CHECK_REPORT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_FREE_MESSAGE)
                .callbackData(CLICK_FREE_MESSAGE));
        return inlineKeyboardMarkup;
    }
}
