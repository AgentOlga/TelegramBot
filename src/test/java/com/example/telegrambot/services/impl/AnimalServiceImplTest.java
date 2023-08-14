package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.Color;
import com.example.telegrambot.constants.PetType;
import com.example.telegrambot.constants.Sex;
import com.example.telegrambot.exception.NotFoundAnimalException;
import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.repository.AnimalRepository;
import com.example.telegrambot.services.ValidationService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnimalServiceImplTest {

    @Mock
    private ValidationService validationService;

    @Mock
    private AnimalRepository animalRepository;

    private AnimalServiceImpl animalService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.animalService = new AnimalServiceImpl(validationService, animalRepository);
    }

    @Test
    public void testSaveAnimal() {
        Animal animal = new Animal("Nick", PetType.DOG, Color.BLACK, Sex.MEN);

        when(validationService.validate(animal)).thenReturn(true);
        when(animalRepository.save(animal)).thenReturn(animal);

        Animal result = animalService.saveAnimal(animal.getNickName(), animal.getPetType(), animal.getColor(), animal.getSex());

        verify(validationService, times(1)).validate(animal);
        verify(animalRepository, times(1)).save(animal);

        assertEquals(animal, result);
    }

    @Test
    public void testSaveAnimalWithInvalidData() {
        Animal animal = new Animal("Nick", PetType.DOG, Color.BLACK, Sex.MEN);

        when(validationService.validate(animal)).thenReturn(false);


    }

    @Test
    public void testUpdateAnimalById() {
        Long id = 1L;
        String nickName = "Nick";
        PetType petType = PetType.DOG;
        Color color = Color.BLACK;
        Sex sex = Sex.MEN;

        Animal animal = new Animal("Nick", PetType.DOG, Color.BLACK, Sex.MEN);

        when(animalRepository.getReferenceById(id)).thenReturn(animal);

        animalService.updateAnimalById(id, nickName, petType, color, sex);

        verify(animalRepository, times(1)).getReferenceById(id);
        verify(animalRepository, times(1)).updateAnimalById(id, nickName, petType, color, sex);

        assertEquals(nickName, animal.getNickName());
        assertEquals(petType, animal.getPetType());
        assertEquals(color, animal.getColor());
        assertEquals(sex, animal.getSex());
    }

    @Test
    public void testUpdateAnimalByIdWithNonExistentAnimal() {

        Long id = 1L;


        when(animalRepository.getReferenceById(id)).thenReturn(null);

    }

    @Test
    public void testDeleteAnimalById() {
        Long id = 1L;

        Animal animal = new Animal("Nick", PetType.DOG, Color.BLACK, Sex.MEN);

        when(animalRepository.getReferenceById(id)).thenReturn(animal);

        animalService.deleteAnimalById(id);

        verify(animalRepository, times(1)).getReferenceById(id);
        verify(animalRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteAnimalByIdWithNonExistentAnimal() {
        Long id = 1L;

        when(animalRepository.getReferenceById(id)).thenReturn(null);

    }

    @Test
    public void testGetAllAnimal() {
        Animal animal1 = new Animal("Nick", PetType.DOG, Color.BLACK, Sex.MEN);
        Animal animal2 = new Animal("Tom", PetType.CAT, Color.WHITE, Sex.WOMEN);

        when(animalRepository.findAll()).thenReturn(Lists.newArrayList(animal1, animal2));

        Collection<Animal> result = animalService.getAllAnimal();

        verify(animalRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals(true, result.contains(animal1));
        assertEquals(true, result.contains(animal2));
    }
}