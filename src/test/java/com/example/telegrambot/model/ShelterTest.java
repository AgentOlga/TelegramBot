package com.example.telegrambot.model;

import com.example.telegrambot.constants.ConstantValue;
import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShelterTest {

    private static final String CORRECT_ADDRESS = "address";
    private static final String CORRECT_TIME_WORK = "timework";
    private static final String CORRECT_DRIVING = "map";
    private static final String CORRECT_PHONE_NUMBER = "1234567891";
    private static final ShelterType CORRECT_SHELTER_TYPE = ShelterType.DOG_SHELTER;
    private static final String EMPTY_VALUE = "";
    private static final String BLANK_VALUE = " ";
    private Shelter shelter;
    private Shelter shelterCorrect;

    @BeforeEach
    public void initTest() {
        shelterCorrect = new Shelter(CORRECT_ADDRESS,
                CORRECT_TIME_WORK,
                CORRECT_DRIVING,
                CORRECT_PHONE_NUMBER,
                CORRECT_PHONE_NUMBER,
                CORRECT_SHELTER_TYPE);

        shelter = new Shelter(CORRECT_ADDRESS,
                CORRECT_TIME_WORK,
                CORRECT_DRIVING,
                CORRECT_PHONE_NUMBER,
                CORRECT_PHONE_NUMBER,
                CORRECT_SHELTER_TYPE);

    }
    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }

    @Test
    public void checkingForIncomingCorrectDataFromTheMethodAddShelter() {
        assertEquals(shelterCorrect, shelter);
    }

    @Test
    public void shouldAddShelterWithParameters() {

        assertNotNull(shelter.getAddressShelter());
        assertNotNull(shelter.getTimeWork());
        assertNotNull(shelter.getDrivingDirections());
        assertNotNull(shelter.getPhoneShelter());
        assertNotNull(shelter.getPhoneSecurity());
        assertNotNull(shelter.getShelterType());
    }

    @Test
    public void shouldAddGuestParametersWithIsBlank() {

        assertThrows(RuntimeException.class,
                () -> new Shelter(BLANK_VALUE,
                        BLANK_VALUE,
                        BLANK_VALUE,
                        BLANK_VALUE,
                        BLANK_VALUE,
                        CORRECT_SHELTER_TYPE));

    }

    @Test
    public void shouldAddGuestParametersWithIsEmpty() {

        assertThrows(RuntimeException.class,
                () -> new Shelter(EMPTY_VALUE,
                        EMPTY_VALUE,
                        EMPTY_VALUE,
                        EMPTY_VALUE,
                        EMPTY_VALUE,
                        CORRECT_SHELTER_TYPE));

    }
}