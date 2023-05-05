package com.example.telegrambot.model;

import com.example.telegrambot.constants.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    private static final String CORRECT_NICK_NAME = "Nick";
    private static final Color CORRECT_COLOR = Color.BLACK;
    private static final Sex CORRECT_SEX = Sex.MEN;
    private static final PetType CORRECT_PET_TYPE = PetType.DOG;
    private Animal animal;
    private Animal animalCorrect;

    @BeforeEach
    public void initTest() {
        animalCorrect = new Animal(CORRECT_NICK_NAME,
                CORRECT_PET_TYPE,
                CORRECT_COLOR,
                CORRECT_SEX);

        animal = new Animal(CORRECT_NICK_NAME,
                CORRECT_PET_TYPE,
                CORRECT_COLOR,
                CORRECT_SEX);
    }

    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }

    @Test
    public void checkingForIncomingCorrectDataFromTheMethodAddUser() {
        assertEquals(animalCorrect, animal);
    }

    @Test
    public void shouldAddUserWithParameters() {

        assertNotNull(animal.getNickName());
        assertNotNull(animal.getPetType());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getSex());
    }


    @Test
    void setNickName_NullValue_ThrowsException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new Animal(null, PetType.DOG, Color.BLACK, Sex.MEN)
        );
    }

    @Test
    void setNickName_EmptyValue_ThrowsException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new Animal("", PetType.DOG, Color.BLACK, Sex.MEN)
        );
    }

    @Test
    void setNickName_BlankValue_ThrowsException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new Animal("    ", PetType.DOG, Color.BLACK, Sex.MEN)
        );
    }

    @Test
    void setPetType_NullValue_ThrowsException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new Animal("Buddy", null, Color.BLACK, Sex.MEN)
        );
    }

    @Test
    void setColor_NullValue_ThrowsException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new Animal("Buddy", PetType.DOG, null, Sex.MEN)
        );
    }

    @Test
    void setSex_NullValue_ThrowsException() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new Animal("Buddy", PetType.DOG, Color.BLACK, null)
        );
    }
}

