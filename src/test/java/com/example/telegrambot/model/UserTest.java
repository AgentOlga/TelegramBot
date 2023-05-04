package com.example.telegrambot.model;

import com.example.telegrambot.constants.ConstantValue;
import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private User userCorrect;
    private User userWrong;
    private User guest;
    private User guestCorrect;
    private User guestWrong;
    private User adopter;
    private User adopterCorrect;
    private User adopterWrong;


    @BeforeEach
    public void initTest() {
        userCorrect = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);

        userWrong = new User(CORRECT_USER_ID, BLANK_VALUE, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);

        user = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);

        guestCorrect = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        guestWrong = new User(CORRECT_USER_ID, BLANK_VALUE, CORRECT_FIRST_NAME,
                CORRECT_FIRST_NAME, CORRECT_PHONE_NUMBER, BLANK_VALUE,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        guest = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        adopterCorrect = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_MAIL, CORRECT_ADDRESS,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        adopterWrong = new User(CORRECT_USER_ID, BLANK_VALUE, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, BLANK_VALUE,
                CORRECT_MAIL, CORRECT_ADDRESS,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        adopter = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_MAIL, CORRECT_ADDRESS,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

    }

    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }

    @Test
    public void checkingForIncomingCorrectDataFromTheMethodAddUser() {
        assertEquals(userCorrect, user);
    }

    @Test
    public void CheckingForIncomingWrongDataFromTheMethodAddUser() {
        assertNotEquals(userCorrect, userWrong);
    }

    @Test
    public void checkingForIncomingCorrectDataFromTheMethodAddGuest() {
        assertEquals(guestCorrect, guest);
    }

    @Test
    public void CheckingForIncomingWrongDataFromTheMethodAddGuest() {
        assertNotEquals(guestCorrect, guestWrong);
    }
    @Test
    public void checkingForIncomingCorrectDataFromTheMethodAddAdopter() {
        assertEquals(adopterCorrect, adopter);
    }

    @Test
    public void CheckingForIncomingWrongDataFromTheMethodAddAdopter() {
        assertNotEquals(adopterCorrect, adopterWrong);
    }

    @Test
    public void shouldAddUserWithParameters() {

        assertNotNull(user.getTelegramId());
        assertNotNull(user.getTelegramNick());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserStatus());
    }

    @Test
    public void shouldAddGuestWithParameters() {

        assertNotNull(guest.getTelegramId());
        assertNotNull(guest.getTelegramNick());
        assertNotNull(guest.getUserType());
        assertNotNull(guest.getUserStatus());
        assertNotNull(guest.getFirstName());
        assertNotNull(guest.getLastName());
        assertNotNull(guest.getPhoneNumber());
        assertNotNull(guest.getCarNumber());
        assertNotNull(guest.getShelterType());
    }

    @Test
    public void shouldAddAdopterWithParameters() {

        assertNotNull(adopter.getTelegramId());
        assertNotNull(adopter.getTelegramNick());
        assertNotNull(adopter.getUserType());
        assertNotNull(adopter.getUserStatus());
        assertNotNull(adopter.getFirstName());
        assertNotNull(adopter.getLastName());
        assertNotNull(adopter.getPhoneNumber());
        assertNotNull(adopter.getCarNumber());
        assertNotNull(adopter.getShelterType());
        assertNotNull(adopter.getAddress());
        assertNotNull(adopter.getEmail());
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

        assertEquals(guestWrong.getCarNumber(), NOT_CAR);
        assertEquals(guestWrong.getTelegramNick(), ConstantValue.DEFAULT_TELEGRAM_NICK);
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