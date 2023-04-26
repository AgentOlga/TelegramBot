package com.example.telegrambot.services.animalsService.impl;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
import com.example.telegrambot.exeption.ValidationAmimalExeption;
import com.example.telegrambot.model.AnimalsShelter;
import com.example.telegrambot.services.animalsService.ValidationAnimal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidationServiceAnimalImpl implements ValidationAnimal {

    /**
     * метод проверяет если какое-то из значений пустое то пробрасывает исключение
     * иначе возращает не пустые значения
     * @param animalsShelter
     * @return
     */
    @Override
    public boolean validateAnimal(AnimalsShelter animalsShelter) {
        if (animalsShelter.getAnimals() == null
                || animalsShelter.getAnimals().getId() <= 0
                || animalsShelter.getAnimals().getColor() == null
                || animalsShelter.getAnimals().getNickName() == null
                || animalsShelter.getAnimals().getPetType() == null
                || animalsShelter.getAnimals().getSex() == null) {
            throw new ValidationAmimalExeption("значение пустое");
        } else {
            return animalsShelter.getAnimals() != null
                    && animalsShelter.getAnimals().getId() >= 0
                    && animalsShelter.getAnimals().getColor() != null
                    && animalsShelter.getAnimals().getNickName() != null
                    && animalsShelter.getAnimals().getPetType() != null
                    && animalsShelter.getAnimals().getSex() != null;
        }
    }
}
