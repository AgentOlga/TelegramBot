package com.example.telegrambot.services.impl;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;

import static com.example.telegrambot.constants.ConstantValue.*;
import static org.junit.jupiter.api.Assertions.*;

class InlineKeyboardMarkupServiceImplTest {

    @Test
    public void testCreateButtonsShelterTypeSelect() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsShelterTypeSelect();
        assertNotNull(markup);
        assertEquals(2, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_CAT_SHELTER, button1.text());
        assertEquals(CLICK_CAT_SHELTER, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_DOG_SHELTER, button2.text());
        assertEquals(CLICK_DOG_SHELTER, button2.callbackData());
    }

    @Test
    public void createButtonsCatShelterTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCatShelter();
        assertNotNull(markup);
        assertEquals(5, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_SHELTER_INFO, button1.text());
        assertEquals(CLICK_CAT_SHELTER_INFO, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_HOW_TO_ADOPT, button2.text());
        assertEquals(CLICK_HOW_TO_ADOPT_A_CAT, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_SEND_REPORT, button3.text());
        assertEquals(CLICK_SEND_A_CAT_REPORT, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_CALL_VOLUNTEER, button4.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_BACK, button5.text());
        assertEquals(CLICK_BACK_TO_SHELTER_TYPE, button5.callbackData());
    }

    @Test
    public void createButtonsDogShelterTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsDogShelter();
        assertNotNull(markup);
        assertEquals(5, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_SHELTER_INFO, button1.text());
        assertEquals(CLICK_DOG_SHELTER_INFO, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_HOW_TO_ADOPT, button2.text());
        assertEquals(CLICK_HOW_TO_ADOPT_A_DOG, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_SEND_REPORT, button3.text());
        assertEquals(CLICK_SEND_A_DOG_REPORT, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_CALL_VOLUNTEER, button4.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_BACK, button5.text());
        assertEquals(CLICK_BACK_TO_SHELTER_TYPE, button5.callbackData());
    }

    @Test
    public void createButtonsCatShelterInfoTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCatShelterInfo();
        assertNotNull(markup);
        assertEquals(6, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_SHELTER_OVERVIEW, button1.text());
        assertEquals(CLICK_CAT_SHELTER_OVERVIEW, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_CAR_PASS, button2.text());
        assertEquals(CLICK_CAR_PASS_CAT, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_SAFETY_PRECAUTIONS, button3.text());
        assertEquals(CLICK_SAFETY_PRECAUTIONS_CAT, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_VISIT, button4.text());
        assertEquals(CLICK_VISIT_CAT, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_CALL_VOLUNTEER, button5.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button5.callbackData());

        InlineKeyboardButton button6 = markup.inlineKeyboard()[5][0];
        assertNotNull(button6);
        assertEquals(BUTTON_BACK, button6.text());
        assertEquals(CLICK_CAT_SHELTER, button6.callbackData());
    }

    @Test
    public void createButtonsDogShelterInfoTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsDogShelterInfo();
        assertNotNull(markup);
        assertEquals(6, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_SHELTER_OVERVIEW, button1.text());
        assertEquals(CLICK_DOG_SHELTER_OVERVIEW, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_CAR_PASS, button2.text());
        assertEquals(CLICK_CAR_PASS_DOG, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_SAFETY_PRECAUTIONS, button3.text());
        assertEquals(CLICK_SAFETY_PRECAUTIONS_DOG, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_VISIT, button4.text());
        assertEquals(CLICK_VISIT_DOG, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_CALL_VOLUNTEER, button5.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button5.callbackData());

        InlineKeyboardButton button6 = markup.inlineKeyboard()[5][0];
        assertNotNull(button6);
        assertEquals(BUTTON_BACK, button6.text());
        assertEquals(CLICK_DOG_SHELTER, button6.callbackData());
    }

    @Test
    public void createButtonsCatShelterTakeTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCatShelterTake();
        assertNotNull(markup);
        assertEquals(7, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_GREETING_WITH_PET, button1.text());
        assertEquals(CLICK_GREETING_WITH_CAT, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_DOC_PET, button2.text());
        assertEquals(CLICK_DOC_CAT, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_TRANSPORT_PET, button3.text());
        assertEquals(CLICK_TRANSPORT_CAT, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_ARRANGEMENT_PET_HOME, button4.text());
        assertEquals(CLICK_ARRANGEMENT_CAT_HOME, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_REASONS_FOR_REFUSAL, button5.text());
        assertEquals(CLICK_REASONS_FOR_REFUSAL_CAT, button5.callbackData());

        InlineKeyboardButton button6 = markup.inlineKeyboard()[5][0];
        assertNotNull(button6);
        assertEquals(BUTTON_CALL_VOLUNTEER, button6.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button6.callbackData());

        InlineKeyboardButton button7 = markup.inlineKeyboard()[6][0];
        assertNotNull(button7);
        assertEquals(BUTTON_BACK, button7.text());
        assertEquals(CLICK_CAT_SHELTER, button7.callbackData());
    }

    @Test
    public void createButtonsDogShelterTakeTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsDogShelterTake();
        assertNotNull(markup);
        assertEquals(9, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_GREETING_WITH_PET, button1.text());
        assertEquals(CLICK_GREETING_WITH_DOG, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_DOC_PET, button2.text());
        assertEquals(CLICK_DOC_DOG, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_TRANSPORT_PET, button3.text());
        assertEquals(CLICK_TRANSPORT_DOG, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_ARRANGEMENT_PET_HOME, button4.text());
        assertEquals(CLICK_ARRANGEMENT_DOG_HOME, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_REASONS_FOR_REFUSAL, button5.text());
        assertEquals(CLICK_REASONS_FOR_REFUSAL_DOG, button5.callbackData());

        InlineKeyboardButton button6 = markup.inlineKeyboard()[5][0];
        assertNotNull(button6);
        assertEquals(BUTTON_CYNOLOGIST_ADVICE, button6.text());
        assertEquals(CLICK_CYNOLOGIST_ADVICE, button6.callbackData());

        InlineKeyboardButton button7 = markup.inlineKeyboard()[6][0];
        assertNotNull(button7);
        assertEquals(BUTTON_FIND_CYNOLOGIST, button7.text());
        assertEquals(CLICK_FIND_CYNOLOGIST, button7.callbackData());

        InlineKeyboardButton button8 = markup.inlineKeyboard()[7][0];
        assertNotNull(button8);
        assertEquals(BUTTON_CALL_VOLUNTEER, button8.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button8.callbackData());

        InlineKeyboardButton button9 = markup.inlineKeyboard()[8][0];
        assertNotNull(button9);
        assertEquals(BUTTON_BACK, button9.text());
        assertEquals(CLICK_DOG_SHELTER, button9.callbackData());
    }

    @Test
    public void createButtonsCatShelterReportTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCatShelterReport();
        assertNotNull(markup);
        assertEquals(4, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_RULES_REPORT_PET, button1.text());
        assertEquals(CLICK_RULES_REPORT_CAT, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_REPORT_PET, button2.text());
        assertEquals(CLICK_REPORT_CAT, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_CALL_VOLUNTEER, button3.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_BACK, button4.text());
        assertEquals(CLICK_CAT_SHELTER, button4.callbackData());
    }

    @Test
    public void createButtonsDogShelterReportTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsDogShelterReport();
        assertNotNull(markup);
        assertEquals(4, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_RULES_REPORT_PET, button1.text());
        assertEquals(CLICK_RULES_REPORT_DOG, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_REPORT_PET, button2.text());
        assertEquals(CLICK_REPORT_DOG, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_CALL_VOLUNTEER, button3.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_BACK, button4.text());
        assertEquals(CLICK_DOG_SHELTER, button4.callbackData());
    }

    @Test
    public void createButtonsCatAtHomeTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCatAtHome();
        assertNotNull(markup);
        assertEquals(5, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_KITTY, button1.text());
        assertEquals(CLICK_KITTY, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_CAT_BIG, button2.text());
        assertEquals(CLICK_CAT_BIG, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_PET_INVALID, button3.text());
        assertEquals(CLICK_CAT_INVALID, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_CALL_VOLUNTEER, button4.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_BACK, button5.text());
        assertEquals(CLICK_HOW_TO_ADOPT_A_CAT, button5.callbackData());
    }

    @Test
    public void createButtonsDogAtHomeTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsDogAtHome();
        assertNotNull(markup);
        assertEquals(5, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_PUPPY, button1.text());
        assertEquals(CLICK_PUPPY, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_DOG_BIG, button2.text());
        assertEquals(CLICK_DOG_BIG, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_PET_INVALID, button3.text());
        assertEquals(CLICK_DOG_INVALID, button3.callbackData());

        InlineKeyboardButton button4 = markup.inlineKeyboard()[3][0];
        assertNotNull(button4);
        assertEquals(BUTTON_CALL_VOLUNTEER, button4.text());
        assertEquals(CLICK_CALL_A_VOLUNTEER, button4.callbackData());

        InlineKeyboardButton button5 = markup.inlineKeyboard()[4][0];
        assertNotNull(button5);
        assertEquals(BUTTON_BACK, button5.text());
        assertEquals(CLICK_HOW_TO_ADOPT_A_DOG, button5.callbackData());
    }

    @Test
    public void createButtonsVolunteerMenuTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsVolunteerMenu();
        assertNotNull(markup);
        assertEquals(3, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_USER_MENU, button1.text());
        assertEquals(CLICK_BACK_TO_SHELTER_TYPE, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_RECORDING_NEW_ANIMAL, button2.text());
        assertEquals(CLICK_RECORDING_NEW_ANIMAL, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_CHECK_REPORT, button3.text());
        assertEquals(CLICK_CHECK_REPORT, button3.callbackData());
    }

    @Test
    public void createButtonsCheckReportTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCheckReport();
        assertNotNull(markup);
        assertEquals(3, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_FREE_MESSAGE, button1.text());
        assertEquals(CLICK_FREE_MESSAGE, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_OK, button2.text());
        assertEquals(CLICK_OK, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_NOT_OK, button3.text());
        assertEquals(CLICK_NOT_OK, button3.callbackData());
    }

    @Test
    public void createButtonsCheckReportNotOkTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCheckReportNotOk();
        assertNotNull(markup);
        assertEquals(3, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_WARNING_REPORT, button1.text());
        assertEquals(CLICK_WARNING_REPORT, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_EXTEND, button2.text());
        assertEquals(CLICK_EXTEND, button2.callbackData());

        InlineKeyboardButton button3 = markup.inlineKeyboard()[2][0];
        assertNotNull(button3);
        assertEquals(BUTTON_DELETE_ADOPTER, button3.text());
        assertEquals(CLICK_DELETE_ADOPTER, button3.callbackData());
    }

    @Test
    public void createButtonsCheckReportNotOkExtendTest() {

        InlineKeyboardMarkupServiceImpl service = new InlineKeyboardMarkupServiceImpl();
        InlineKeyboardMarkup markup = service.createButtonsCheckReportNotOkExtend();
        assertNotNull(markup);
        assertEquals(2, markup.inlineKeyboard().length);

        InlineKeyboardButton button1 = markup.inlineKeyboard()[0][0];
        assertNotNull(button1);
        assertEquals(BUTTON_EXTEND_14_DAY, button1.text());
        assertEquals(CLICK_EXTEND_14_DAY, button1.callbackData());

        InlineKeyboardButton button2 = markup.inlineKeyboard()[1][0];
        assertNotNull(button2);
        assertEquals(BUTTON_EXTEND_30_DAY, button2.text());
        assertEquals(CLICK_EXTEND_30_DAY, button2.callbackData());
    }
}