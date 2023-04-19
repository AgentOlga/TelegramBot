package com.example.telegrambot.services.animalsService;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
import com.example.telegrambot.model.AnimalsShelter;
import org.springframework.stereotype.Service;

@Service
public interface AnimalService {
    void addAnimal(AnimalsShelter animalsShelter);

    int issuance(AnimalsShelter animalsShelter);

    int getCount(PetType petType, Sex sex,
                 Color color);

    String deleteAnimal( AnimalsShelter animalsShelter);
}
