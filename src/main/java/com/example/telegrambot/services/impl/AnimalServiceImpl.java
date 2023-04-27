package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.Color;
import com.example.telegrambot.constants.PetType;
import com.example.telegrambot.constants.Sex;
import com.example.telegrambot.exception.NotFoundAnimalException;
import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.repository.AnimalRepository;
import com.example.telegrambot.services.AnimalService;
import com.example.telegrambot.services.ValidationService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Бизнес-логика по работе с животными.
 */
@Service
public class AnimalServiceImpl implements AnimalService {

    private final ValidationService validationService;
    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(ValidationService validationService, AnimalRepository animalRepository) {
        this.validationService = validationService;
        this.animalRepository = animalRepository;
    }

    @Override
    public Animal saveAnimal(String nickName,
                             PetType petType,
                             Color color,
                             Sex sex) {

        Animal animal = new Animal(nickName, petType, color, sex);

        if (!validationService.validate(animal)) {
            throw new ValidationException(animal.toString());
        }
        return animalRepository.save(animal);
    }

    @Override
    public void updateAnimalById(Long id,
                                 String nickName,
                                 PetType petType,
                                 Color color,
                                 Sex sex) {
        Animal animal = animalRepository.getReferenceById(id);
        if (animal == null) {
            throw new NotFoundAnimalException("Животное не найдено!");
        }
        animalRepository.updateAnimalById(id, nickName, petType, color, sex);
    }

    @Override
    public void deleteAnimalById(Long id) {

        Animal animal = animalRepository.getReferenceById(id);
        if (animal == null) {
            throw new NotFoundAnimalException("Животное не найдено!");
        }
        animalRepository.deleteById(id);
    }

    @Override
    public Collection<Animal> getAllAnimal() {
        return animalRepository.findAll();
    }
}
