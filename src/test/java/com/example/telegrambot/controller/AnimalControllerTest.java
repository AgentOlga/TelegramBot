package com.example.telegrambot.controller;

import com.example.telegrambot.constants.Color;
import com.example.telegrambot.constants.PetType;
import com.example.telegrambot.constants.Sex;
import com.example.telegrambot.model.Animal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalControllerTest {

    public static final String NICK_NAME = "sharic";
    public static final String NICK_NAME_WRONG = "sharic123";
    public static final Sex SEX_CORRECT_ONE = Sex.valueOf("MEN");
    public static final Sex SEX_CORRECT_TWO = Sex.valueOf("WOMEN");
    public static final PetType PET_CORRECT_ONE = PetType.valueOf("DOG");
    public static final PetType PET_CORRECT_TWO = PetType.valueOf("CAT");
    public static final Color COLOR_CORRECT_ONE = Color.valueOf("BLACK");
    public static final Color COLOR_CORRECT_TWO = Color.valueOf("WHITE");
    public static final Color COLOR_CORRECT_THREE = Color.valueOf("GREY");
    public static final Color COLOR_CORRECT_FOUR = Color.valueOf("ORANGE");
    public static final Color COLOR_CORRECT_FIVE = Color.valueOf("CINNAMON");
    Animal animal;
    Animal animalCorrect;

    Animal animalWrong;

    @BeforeEach
    public void initTest() {
        animalCorrect = new Animal( 3, NICK_NAME,PET_CORRECT_ONE,
                COLOR_CORRECT_ONE,SEX_CORRECT_ONE);

        animalWrong = new Animal( 3, NICK_NAME_WRONG,PET_CORRECT_ONE,
                COLOR_CORRECT_ONE,SEX_CORRECT_ONE);

        animal = new Animal( 3, NICK_NAME,PET_CORRECT_ONE,
                COLOR_CORRECT_ONE,SEX_CORRECT_ONE);

    }

    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }


    @Test
    public void checkingForIncomingCorrectDataFromTheMethodCreateAnimals() {
        assertEquals(animalCorrect, animal);
    }

    @Test
    public void CheckingForIncomingWrongDataFromTheMethodCreateAnimals() {
        assertNotEquals(animalCorrect, animalWrong);
    }

}