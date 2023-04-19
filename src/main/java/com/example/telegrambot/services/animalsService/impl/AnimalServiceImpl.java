package com.example.telegrambot.services.animalsService.impl;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
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
     * метод, который добавляет животное в БД
     * @param animalsShelter
     */
    @Override
    public void addAnimal(AnimalsShelter animalsShelter) {
        checkAnimalsShelter(animalsShelter);
        animalsRepository.save(animalsShelter);
    }


    //
    @Override
    public int issuance(AnimalsShelter animalsShelter) {
        checkAnimalsShelter(animalsShelter);
        return animalsRepository.remove(animalsShelter);
    }
    // метод выводит количество животных
    @Override
    public int getCount(PetType petType, Sex sex,Color color) {
        if (validationServiceAnimal.validateAnimal(petType, color, sex)) {
            Map<Animals, Integer> animalsMap = animalsRepository.getAll();
            int q = 0;
            for (Map.Entry<Animals, Integer> AnimalsItem : animalsMap.entrySet()) {
                Animals animals = AnimalsItem.getKey();
                if (animals.getColor() != null
                        && animals.getPetType() != null
                        && animals.getSex() != null) {
                    q = q + AnimalsItem.getValue();
        } else {
                    throw new ValidationAmimalExeption("ошибка 1");
                }
            }
        }
        return 0;
    }


    /**
     * метод, который удаляет животного из БД
     * @param animalsShelter
     * @return
     */
    @Override
    public String deleteAnimal(AnimalsShelter animalsShelter) {
        checkAnimalsShelter(animalsShelter);
        return animalsRepository.removeOne(animalsShelter);
    }

    /**
     * метод, который проверяет не передаются ли пустые значения животного
     * @param animalsShelter
     */
    private void checkAnimalsShelter (AnimalsShelter animalsShelter) {
        if (!validationServiceAnimal.validateAnimal(animalsShelter)){
            throw new ValidationAmimalExeption("ошибка 2");
        }

    }
}
