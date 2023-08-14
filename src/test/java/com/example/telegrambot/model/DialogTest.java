package com.example.telegrambot.model;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DialogTest {

    private static final LocalDate CORRECT_DATA_REPORT = LocalDate.now();
    private static final String CORRECT_TEXT = "really well";
    private static final long CORRECT_USER_ID = 123456789;
    private static final String CORRECT_USER_NAME = "Nick";
    private static final UserType CORRECT_USER_TYPE = UserType.GUEST;
    private static final UserType CORRECT_USER_TYPE_VOLUNTEER = UserType.VOLUNTEER;
    private static final UserStatus CORRECT_USER_STATUS = UserStatus.APPROVE;
    private static final String CORRECT_FIRST_NAME = "Ivan";
    private static final String CORRECT_LAST_NAME = "Ivanov";
    private static final String CORRECT_PHONE_NUMBER = "1234567891";
    private static final String CORRECT_CAR_NUMBER = "123456";
    private static final ShelterType CORRECT_SHELTER_TYPE = ShelterType.DOG_SHELTER;
    private static final String CORRECT_MAIL = "Ivan@mail.ru";
    private static final String CORRECT_ADDRESS = "Astana, lenina 12";

    private Dialog dialog;
    private Dialog dialogCorrect;
    private User guest;
    private User volunteer;


    @BeforeEach
    public void initTest() {
        dialogCorrect = new Dialog(guest,
                volunteer,
                CORRECT_TEXT,
                CORRECT_DATA_REPORT);

        dialog = new Dialog(guest,
                volunteer,
                CORRECT_TEXT,
                CORRECT_DATA_REPORT);

        guest = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_MAIL, CORRECT_ADDRESS,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        volunteer = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_MAIL, CORRECT_ADDRESS,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE_VOLUNTEER, CORRECT_USER_STATUS);

    }

    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }

    @Test
    public void checkingForIncomingCorrectDataFromTheMethodAddUser() {
        assertEquals(dialogCorrect, dialog);
    }

    @Test
    void testDialog() {
        User user = guest;
        User user2 = volunteer;
        LocalDate date = CORRECT_DATA_REPORT;
        Dialog dialog = new Dialog(user, user2, "Hello, world!", date);

        assertEquals(guest, dialog.getGuestId());
        assertEquals(volunteer, dialog.getVolunteerId());
        assertEquals("Hello, world!", dialog.getTextMessage());
        assertEquals(date, dialog.getDateMessage());
    }

    @Test
    void testConstructorWithNoParameters() {
        //Act
        dialog = new Dialog();

        //Assert
        assertNotNull(dialog);
        assertNull(dialog.getGuestId());
        assertNull(dialog.getVolunteerId());
        assertNull(dialog.getTextMessage());
        assertNull(dialog.getDateMessage());
    }
}
