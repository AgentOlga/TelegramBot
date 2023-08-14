package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.*;
import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.model.User;
import com.example.telegrambot.services.ValidationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceImplTest {

    private ValidationService validationService = new ValidationServiceImpl();

    @Test
    void testValidateUser() {
        User user = new User();
        user.setTelegramId(12345678);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("5559995555");
        user.setEmail("johndoe@example.com");
        user.setUserType(UserType.ADOPTER);
        user.setUserStatus(UserStatus.APPROVE);
        assertTrue(validationService.validate(user));
    }

    @Test
    void testValidateAnimal() {
        Animal animal = new Animal();
        animal.setColor(Color.BLACK);
        animal.setNickName("Fido");
        animal.setPetType(PetType.DOG);
        animal.setSex(Sex.MEN);
        assertTrue(validationService.validate(animal));
    }

    @Test
    void testValidateAdopter() {
        Adopter adopter = new Adopter();
        adopter.setShelter(new Shelter());
        adopter.setAnimal(new Animal());
        assertTrue(validationService.validate(adopter));
    }

    @Test
    void testValidateShelter() {
        Shelter shelter = new Shelter();
        shelter.setAddressShelter("123 Main St.");
        shelter.setTimeWork("8am-6pm");
        shelter.setDrivingDirections("Take the highway...");
        shelter.setPhoneShelter("555-1212");
        shelter.setPhoneSecurity("555-1313");
        shelter.setShelterType(ShelterType.DOG_SHELTER);
        assertTrue(validationService.validate(shelter));
    }

    @Test
    void testValidateInvalidObject() {
        assertFalse(validationService.validate(new Object()));
    }

}