package com.example.telegrambot.services.animalsService;
import com.example.telegrambot.model.Animals;
import com.example.telegrambot.model.AnimalsShelter;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface AnimalService {
    void addAnimal(AnimalsShelter animalsShelter);

    public Map<Animals, Integer> getAllAnimals();

    String deleteAnimal( AnimalsShelter animalsShelter);
}
