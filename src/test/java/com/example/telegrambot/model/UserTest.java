package com.example.telegrambot.model;

import com.example.telegrambot.constants.ConstantValue;
import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final String CORRECT_USER_NAME = "Nick";
    private static final long CORRECT_USER_ID = 123456789;
    private static final UserType CORRECT_USER_TYPE = UserType.DEFAULT;
    private static final UserStatus CORRECT_USER_STATUS = UserStatus.APPROVE;
    private static final String CORRECT_FIRST_NAME = "Ivan";
    private static final String INCORRECT_FIRST_NAME = "ivan";
    private static final String CORRECT_LAST_NAME = "Ivanov";
    private static final String CORRECT_PHONE_NUMBER = "1234567891";
    private static final String INCORRECT_PHONE_NUMBER = "12345678910";
    private static final String CORRECT_CAR_NUMBER = "123456";
    private static final ShelterType CORRECT_SHELTER_TYPE = ShelterType.DOG_SHELTER;
    private static final String CORRECT_MAIL = "Ivan@mail.ru";
    private static final String CORRECT_ADDRESS = "Astana, lenina 12";
    private static final String NOT_CAR = "Без автомобиля";

    private static final String EMPTY_VALUE = "";
    private static final String BLANK_VALUE = " ";
    private User user;


    @Test
    public void shouldAddUserWithParameters() {
        user = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        assertNotNull(user.getUserId());
        assertNotNull(user.getTelegramNick());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserStatus());
    }

    @Test
    public void shouldAddGuestWithParameters() {
        user = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        assertNotNull(user.getUserId());
        assertNotNull(user.getTelegramNick());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserStatus());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
        assertNotNull(user.getPhoneNumber());
        assertNotNull(user.getCarNumber());
        assertNotNull(user.getShelterType());
    }

    @Test
    public void shouldAddAdopterWithParameters() {
        user = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_MAIL, CORRECT_ADDRESS,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        assertNotNull(user.getUserId());
        assertNotNull(user.getTelegramNick());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserStatus());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
        assertNotNull(user.getPhoneNumber());
        assertNotNull(user.getCarNumber());
        assertNotNull(user.getShelterType());
        assertNotNull(user.getAddress());
        assertNotNull(user.getEmail());
    }

    @Test
    public void shouldAddGuestParametersWithIncorrect() {

        // некорректный номер телефона
        assertThrows(RuntimeException.class,
                () -> new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                        CORRECT_LAST_NAME, INCORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                        CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS));

        // некорректное имя
        assertThrows(RuntimeException.class,
                () -> new User(CORRECT_USER_ID, CORRECT_USER_NAME, INCORRECT_FIRST_NAME,
                        CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                        CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS));

    }

    @Test
    public void shouldAddGuestWithParametersIsBlank() {
        user = new User(CORRECT_USER_ID, BLANK_VALUE, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, BLANK_VALUE,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        assertEquals(user.getCarNumber(), NOT_CAR);
        assertEquals(user.getTelegramNick(), ConstantValue.DEFAULT_TELEGRAM_NICK);
    }

    @Test
    public void shouldAddGuestWithParametersIsEmpty() {
        user = new User(CORRECT_USER_ID, EMPTY_VALUE, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, EMPTY_VALUE,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        assertEquals(user.getCarNumber(), NOT_CAR);
        assertEquals(user.getTelegramNick(), ConstantValue.DEFAULT_TELEGRAM_NICK);
    }

}