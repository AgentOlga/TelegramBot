package com.example.telegrambot.services.animalsService.impl;
import com.example.telegrambot.exeption.ValidationAmimalExeption;
import com.example.telegrambot.model.Animals;
import com.example.telegrambot.model.AnimalsShelter;
import com.example.telegrambot.repository.AnimalsRepository;
import com.example.telegrambot.services.animalsService.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@AllArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    private final AnimalsRepository animalsRepository;
    private final ValidationServiceAnimalImpl validationServiceAnimal;

    /**
     * метод проверяет соответствует ли переданные значения требованиями
     * и после этого добавляет животного в БД
     * @param animalsShelter
     */
    @Override
    public void addAnimal(AnimalsShelter animalsShelter) {
        checkAnimalsShelter(animalsShelter);
        animalsRepository.save(animalsShelter);
    }

    /**
     * метод, который выводит список всех животных, которые есть в БД
     * @return
     */
    @Override
    public Map<Animals, Integer> getAllAnimals() {
        return animalsRepository.getAll();
    }


    /**
     * метод, который удаляет животного из БД
     * @param animalsShelter
     * @return
     */
    @Override
    public String deleteAnimal(AnimalsShelter animalsShelter) {
        checkAnimalsShelter(animalsShelter);
        return animalsRepository.remove(animalsShelter);
    }

    /**
     * метод, который проверяет соответствуют ли переданные значения требованиям
     * @param animalsShelter
     */
    private void checkAnimalsShelter (AnimalsShelter animalsShelter) {
        if (!validationServiceAnimal.validateAnimal(animalsShelter)){
            throw new ValidationAmimalExeption("не верно переданы данные о животном");
        }

    }
}
