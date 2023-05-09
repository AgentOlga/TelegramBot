package com.example.telegrambot.controller;

import com.example.telegrambot.constants.Color;
import com.example.telegrambot.constants.PetType;
import com.example.telegrambot.constants.Sex;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.services.AnimalService;
import com.pengrad.telegrambot.TelegramBot;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AnimalControllerTest {

    public static final String NICK_NAME = "Шарик";
    public static final String NICK_NAME_TWO = "Мурзик";
    public static final Sex SEX_CORRECT_ONE = Sex.MEN;
    public static final Sex SEX_CORRECT_TWO = Sex.WOMEN;
    public static final PetType PET_CORRECT_ONE = PetType.DOG;
    public static final PetType PET_CORRECT_TWO = PetType.CAT;
    public static final Color COLOR_CORRECT_ONE = Color.BLACK;
    public static final Color COLOR_CORRECT_TWO = Color.WHITE;

    private static final Long TEST_ID = 1L;

    @Test
    public void saveAnimal_returns200_whenAnimalSavedSuccessfully() {
        AnimalService mockedService = mock(AnimalService.class);
        AnimalController controller = new AnimalController(mockedService);

        Animal animal = new Animal(NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE);
        when(mockedService.saveAnimal(NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE))
                .thenReturn(animal);

        ResponseEntity<Animal> response = controller
                .saveAnimal(NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(animal, response.getBody());
    }
    @Test
    public void saveAnimal_returnsOK_whenParamsAreMissing() {
        AnimalService mockedService = mock(AnimalService.class);
        AnimalController controller = new AnimalController(mockedService);

        ResponseEntity<Animal> response = controller.saveAnimal(null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test public void saveAnimal_returnsOK_whenParamsHaveInvalidFormat() {
        AnimalService mockedService = mock(AnimalService.class);
        AnimalController controller = new AnimalController(mockedService);

        ResponseEntity<Animal> response = controller
                .saveAnimal(NICK_NAME, null, COLOR_CORRECT_ONE, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test public void saveAnimal_returns404_whenServiceThrowsException() {
        AnimalService mockedService = mock(AnimalService.class);
        AnimalController controller = new AnimalController(mockedService);

        when(mockedService.saveAnimal(NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE))
                .thenThrow(new RuntimeException());

        ResponseEntity<Animal> response = controller
                .saveAnimal(NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateAnimalById() {

        AnimalService animalServiceMock = mock(AnimalService.class);
        AnimalController animalController = new AnimalController(animalServiceMock);
        Long id = 1L;

        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

        ResponseEntity<Void> actualResponse = animalController
                .updateAdopter(id, NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE);

        assertEquals(expectedResponse, actualResponse);
        verify(animalServiceMock).updateAnimalById(id, NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE);
    }

    @Mock
    private AnimalService animalService;

    private AnimalController animalController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        animalController = new AnimalController(animalService);
    }

    @Test
    public void testGetAllAnimal() {
        Animal animal1 = new Animal(1, NICK_NAME, PET_CORRECT_ONE, COLOR_CORRECT_ONE, SEX_CORRECT_ONE);
        Animal animal2 = new Animal(2, NICK_NAME_TWO, PET_CORRECT_TWO, COLOR_CORRECT_TWO, SEX_CORRECT_TWO);
        List<Animal> animals = new ArrayList<>();
        animals.add(animal1);
        animals.add(animal2);
        when(animalService.getAllAnimal()).thenReturn(animals);

        ResponseEntity<Collection<Animal>> response = animalController.getAllAnimal();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void checkingDeleteAdopterByIdSuccess() {
        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();
        Mockito.doNothing().when(animalService).deleteAnimalById(TEST_ID);

        ResponseEntity<Void> actualResponse = animalController.deleteAdopterById(TEST_ID);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void checkingDeleteAdopterByIdNotFound() {
        ResponseEntity<Void> expectedResponse = ResponseEntity.notFound().build();
        Mockito.doThrow(new RuntimeException()).when(animalService).deleteAnimalById(TEST_ID);

        ResponseEntity<Void> actualResponse = animalController.deleteAdopterById(TEST_ID);

        assertEquals(expectedResponse, actualResponse);
    }
}