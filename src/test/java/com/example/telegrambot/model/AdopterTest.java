package com.example.telegrambot.model;

import com.example.telegrambot.constants.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdopterTest {

    private static final String CORRECT_ADDRESS = "address";
    private static final String CORRECT_TIME_WORK = "timework";
    private static final String CORRECT_DRIVING = "map";
    private static final String CORRECT_PHONE_NUMBER = "1234567891";
    private static final ShelterType CORRECT_SHELTER_TYPE = ShelterType.DOG_SHELTER;
    private Shelter shelter;
    private static final String CORRECT_USER_NAME = "Nick";
    private static final long CORRECT_USER_ID = 123456789;
    private static final UserType CORRECT_USER_TYPE = UserType.DEFAULT;
    private static final UserStatus CORRECT_USER_STATUS = UserStatus.APPROVE;
    private User user;
    private static final String CORRECT_NICK_NAME = "Nick";
    private static final Color CORRECT_COLOR = Color.BLACK;
    private static final Sex CORRECT_SEX = Sex.MEN;
    private static final PetType CORRECT_PET_TYPE = PetType.DOG;
    private Animal animal;
    private Adopter adopter;
    private Adopter adopterCorrect;
    private Adopter adopterWrong;


    @BeforeEach
    public void initTest() {

        animal = new Animal(CORRECT_NICK_NAME,
                CORRECT_PET_TYPE,
                CORRECT_COLOR,
                CORRECT_SEX);

        user = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);

        shelter = new Shelter(CORRECT_ADDRESS,
                CORRECT_TIME_WORK,
                CORRECT_DRIVING,
                CORRECT_PHONE_NUMBER,
                CORRECT_PHONE_NUMBER,
                CORRECT_SHELTER_TYPE);

        adopter = new Adopter(user,
                animal,
                shelter);

        adopterCorrect = new Adopter(user,
                animal,
                shelter);

        adopterWrong = new Adopter(null,
                null,
                null);
    }

    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }

    @Test
    public void checkingForIncomingCorrectDataFromTheMethod() {
        assertEquals(adopterCorrect, adopter);
    }

    @Test
    public void CheckingForIncomingWrongDataFromTheMethod() {
        assertNotEquals(adopterCorrect, adopterWrong);
    }

    @Test
    public void shouldAdopterWithParameters() {

        assertNotNull(adopter.getUser());
        assertNotNull(adopter.getAnimal());
        assertNotNull(adopter.getShelter());
    }

//    Test creating an Adopter object with a valid set of parameters (User, Animal, Shelter
    @Test
    public void testAdopterCreation() {

        assertEquals(user, adopter.getUser());
        assertEquals(animal, adopter.getAnimal());
        assertEquals(shelter, adopter.getShelter());
    }

//    Test getting the User from an Adopter object
    @Test public void testAdopterGetUser() {

        Adopter adopter = new Adopter(user, animal, shelter);

        assertEquals(user, adopter.getUser());
    }

//    Test getting the Animal from an Adopter object
    @Test public void testAdopterGetAnimal() {

        Adopter adopter = new Adopter(user, animal, shelter);

        assertEquals(animal, adopter.getAnimal());
    }

//    Test getting the Shelter from an Adopter object
    @Test public void testAdopterGetShelter() {

        Adopter adopter = new Adopter(user, animal, shelter);

        assertEquals(shelter, adopter.getShelter());
    }
}