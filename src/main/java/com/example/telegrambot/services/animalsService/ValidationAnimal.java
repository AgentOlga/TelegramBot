package com.example.telegrambot.services.animalsService;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
import com.example.telegrambot.model.AnimalsShelter;
import org.springframework.stereotype.Service;

@Service
public interface ValidationAnimal {
    boolean validateAnimal(AnimalsShelter animalsShelter);

    boolean validateAnimal(PetType petType, Color color, Sex sex);
}
