package com.example.telegrambot.services.animalsService.impl;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
import com.example.telegrambot.model.AnimalsShelter;
import com.example.telegrambot.services.animalsService.ValidationAnimal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidationServiceAnimalImpl implements ValidationAnimal {

    /**
     * метод проверяет не передаются ли значение
     * животных путые
     * id больше меньше нуля
     * нет параментра цвета животного
     * нет параметра никнейма животного
     * нет параментра тип животого
     * нет параметра пола животного
     * @param animalsShelter
     * @return
     */
    @Override
    public boolean validateAnimal(AnimalsShelter animalsShelter) {
        return animalsShelter.getAnimals() != null
                && animalsShelter.getAnimals().getId() >= 0
                && animalsShelter.getAnimals().getColor() != null
                && animalsShelter.getAnimals().getNickName() != null
                && animalsShelter.getAnimals().getPetType() != null
                && animalsShelter.getAnimals().getSex() != null;
    }

    @Override
    public boolean validateAnimal(PetType petType, Color color, Sex sex) {
        return false;
    }


}
