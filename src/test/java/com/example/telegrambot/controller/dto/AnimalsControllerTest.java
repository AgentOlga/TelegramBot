package com.example.telegrambot.controller.dto;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
import com.example.telegrambot.model.Animals;
import com.example.telegrambot.model.AnimalsShelter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimalsControllerTest {
    public static final String RESPONCE_DTO = "животное успешно сохранено";
    public static final String RESPONCE_DTO_TWO = " удален(a) из БД";
    public static final int CORRECT_ID = 1;

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
    Animals animals;
    Animals animalsCorrect;

    Animals animalsWrong;
    AnimalsShelter animalsShelter;
    AnimalsShelter animalsShelterCorrect;
    AnimalsShelter animalsShelterWrong;

    @BeforeEach
    public void initTest() {
        animalsCorrect = new Animals( 3, NICK_NAME,PET_CORRECT_ONE,
                COLOR_CORRECT_ONE,SEX_CORRECT_ONE);
        animalsShelterCorrect = new AnimalsShelter(animalsCorrect, CORRECT_ID);
        animalsWrong = new Animals( 3, NICK_NAME_WRONG,PET_CORRECT_ONE,
                COLOR_CORRECT_ONE,SEX_CORRECT_ONE);

        animals = new Animals( 3, NICK_NAME,PET_CORRECT_ONE,
                COLOR_CORRECT_ONE,SEX_CORRECT_ONE);
        animalsShelter = new AnimalsShelter(animals, 1);
        animalsShelterWrong = new AnimalsShelter(animalsWrong, 2);
    }

    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }


    @Test
    public void checkingForIncomingCorrectDataFromTheMethodCreateAnimals() {
        assertEquals(animalsCorrect, animals);
    }

    @Test
    public void CheckingForIncomingWrongDataFromTheMethodCreateAnimals() {
        assertNotEquals(animalsCorrect, animalsWrong);
    }

    @Test
    public void checkingTheFinalMessageInTheControllerCreateAnimals() {
        assertEquals(RESPONCE_DTO, "животное успешно сохранено");
    }

    @Test
    public void checkingTheFinalMessageInTheControllerDeleteAnimals() {
        assertEquals(NICK_NAME + RESPONCE_DTO_TWO , animals.getNickName() +
                " удален(a) из БД");

    }

    @Test
    public void verificationInTheMethodGetAllAnimalsIfThereAreNoAnimalsAndAnEmptyOneIsPassed() {
        Map<Animals, Integer> animalsMap = null;
        assertNull(animalsMap);
    }

}
