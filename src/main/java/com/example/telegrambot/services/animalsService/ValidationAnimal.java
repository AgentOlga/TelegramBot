package com.example.telegrambot.services.animalsService;
import com.example.telegrambot.model.AnimalsShelter;
import org.springframework.stereotype.Service;

@Service
public interface ValidationAnimal {
    boolean validateAnimal(AnimalsShelter animalsShelter);
}
