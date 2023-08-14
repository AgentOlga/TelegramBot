package com.example.telegrambot.controller;

import com.example.telegrambot.constants.*;
import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.model.User;
import com.example.telegrambot.services.AdopterService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdopterControllerTest {

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
    private static final Long CORRECT_TEST_ID = 1L;

    @MockBean
    private AdopterService adopterService;
    private AdopterController adopterController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        adopterController = new AdopterController(adopterService);
    }

    @Test
    public void saveNewAdopterReturnsOk() throws Exception {

        AdopterController controller = new AdopterController(adopterService);

        animal = new Animal(CORRECT_NICK_NAME,
                CORRECT_PET_TYPE,
                CORRECT_COLOR,
                CORRECT_SEX);

        user = new User(CORRECT_USER_ID,
                CORRECT_USER_NAME,
                CORRECT_USER_TYPE,
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

        when(adopterService.saveAdopter(adopter))
                .thenReturn(adopter);

        ResponseEntity<Adopter> response = controller
                .saveNewAdopter(adopter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adopter, response.getBody());
    }

    @Test
    public void testGetAllAdopter() {

        Adopter adopter1 = new Adopter(user, animal, shelter);
        List<Adopter> adopters = new ArrayList<>();
        adopters.add(adopter);
        adopters.add(adopter1);
        when(adopterService.getAllAdopter()).thenReturn(adopters);

        ResponseEntity<Collection<Adopter>> response = adopterController.getAllAdopter();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
    @Test
    public void checkingFoundAdopterByIdSuccess() {
        Adopter testAdopter = new Adopter();
        testAdopter.setId(CORRECT_TEST_ID);
        ResponseEntity<Adopter> expectedResponse = ResponseEntity.ok(testAdopter);
        Mockito.when(adopterService.foundAdopterById(CORRECT_TEST_ID)).thenReturn(testAdopter);

        ResponseEntity<Adopter> actualResponse = adopterController.foundAdopterById(CORRECT_TEST_ID);

        assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void checkingFoundAdopterByIdNotFound() {
        ResponseEntity<Adopter> expectedResponse = ResponseEntity.notFound().build();
        Mockito.doThrow(new RuntimeException()).when(adopterService).foundAdopterById(CORRECT_TEST_ID);

        ResponseEntity<Adopter> actualResponse = adopterController.foundAdopterById(CORRECT_TEST_ID);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void checkingGetAllAdoptersNotFound() {
        ResponseEntity<Collection<Adopter>> expectedResponse = ResponseEntity.notFound().build();
        Mockito.doThrow(new RuntimeException()).when(adopterService).getAllAdopter();

        ResponseEntity<Collection<Adopter>> actualResponse = adopterController.getAllAdopter();

        assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void checkingDeleteAdopterByIdSuccess() {
        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

        ResponseEntity<Void> actualResponse = adopterController.deleteAdopterById(CORRECT_TEST_ID);

        Mockito.verify(adopterService).deleteAdopterById(CORRECT_TEST_ID);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void checkingDeleteAdopterByIdNotFound() {
        ResponseEntity<Void> expectedResponse = ResponseEntity.notFound().build();
        Mockito.doThrow(new RuntimeException()).when(adopterService).deleteAdopterById(CORRECT_TEST_ID);

        ResponseEntity<Void> actualResponse = adopterController.deleteAdopterById(CORRECT_TEST_ID);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void checkingUpdateAdopterNotFound() {
        User user = new User();
        Animal animal = new Animal();
        Shelter shelter = new Shelter();
        ResponseEntity<Void> expectedResponse = ResponseEntity.notFound().build();
        Mockito.doThrow(new RuntimeException()).when(adopterService).updateAdopterById(CORRECT_TEST_ID, user, animal, shelter);

        ResponseEntity<Void> actualResponse = adopterController.updateAdopter(CORRECT_TEST_ID, user, animal, shelter);

        assertEquals(expectedResponse, actualResponse);
    }
}