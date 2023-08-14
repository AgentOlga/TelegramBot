package com.example.telegrambot.services.impl;

import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.repository.AdopterRepository;
import com.example.telegrambot.repository.AnimalRepository;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.AdopterService;
import com.example.telegrambot.services.ValidationService;
import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdopterServiceImplTest {

    @Test
    public void testSaveAdopter() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Adopter adopter = new Adopter();

// mock behavior
        Mockito.when(mockValidationService.validate(adopter)).thenReturn(true);
        Mockito.when(mockAdopterRepository.save(adopter)).thenReturn(adopter);

// perform the test
        Adopter result = adopterService.saveAdopter(adopter);

// verify the result
        assertEquals(adopter, result);
    }
    @Test
    public void testSaveAdopterWithInvalidData() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Adopter adopter = new Adopter();

// mock behavior
        Mockito.when(mockValidationService.validate(adopter)).thenReturn(false);

    }
    @Test
    public void testUpdateAdopterById() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Long id = 1L;
        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User();
        Animal animal = new Animal();
        Shelter shelter = new Shelter();

// mock behavior
        Mockito.when(mockAdopterRepository.getReferenceById(id)).thenReturn(new Adopter());

// perform the test
        adopterService.updateAdopterById(id, user, animal, shelter);

// verify the behavior
        Mockito.verify(mockAdopterRepository).updateAdopterById(id, user, animal, shelter);
    }
    @Test
    public void testUpdateAdopterByIdWithNonExistingAdopter() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Long id = 1L;
        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User();
        Animal animal = new Animal();
        Shelter shelter = new Shelter();

// mock behavior
        Mockito.when(mockAdopterRepository.getReferenceById(id)).thenReturn(null);

    }
    @Test
    public void testDeleteAdopterById() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Long id = 1L;

// mock behavior
        Mockito.when(mockAdopterRepository.getReferenceById(id)).thenReturn(new Adopter());

// perform the test
        adopterService.deleteAdopterById(id);

// verify the behavior
        Mockito.verify(mockAdopterRepository).delete(Mockito.any(Adopter.class));
    }
    @Test
    public void testDeleteAdopterByIdWithNonExistingAdopter() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Long id = 1L;

// mock behavior
        Mockito.when(mockAdopterRepository.getReferenceById(id)).thenReturn(null);

    }
    @Test public void testGetAllAdopter() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Collection<Adopter> expected = new ArrayList<>();
        expected.add(new Adopter());

// mock behavior
        Mockito.when(mockAdopterRepository.findAll()).thenReturn((List<Adopter>) expected);

// perform the test
        Collection<Adopter> result = adopterService.getAllAdopter();

// verify the result
        assertEquals(expected, result);
    }
    @Test
    public void testFoundAdopterById() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Long id = 1L;
        Adopter expected = new Adopter();

// mock behavior
        Mockito.when(mockAdopterRepository.getReferenceById(id)).thenReturn(expected);

// perform the test
        Adopter result = adopterService.foundAdopterById(id);

// verify the result
        assertEquals(expected, result);
    }
    @Test
    public void testFoundAdopterByIdWithNonExistingAdopter() {
// create mock objects
        AdopterRepository mockAdopterRepository = Mockito.mock(AdopterRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        AnimalRepository mockAnimalRepository = Mockito.mock(AnimalRepository.class);
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);

// create test data
        AdopterService adopterService = new AdopterServiceImpl(mockAdopterRepository, mockUserRepository, mockAnimalRepository, mockValidationService);
        Long id = 1L;

// mock behavior
        Mockito.when(mockAdopterRepository.getReferenceById(id)).thenReturn(null);

    }

}