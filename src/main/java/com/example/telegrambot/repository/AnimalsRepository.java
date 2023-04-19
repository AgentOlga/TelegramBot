package com.example.telegrambot.repository;

import com.example.telegrambot.model.Animals;
import com.example.telegrambot.model.AnimalsShelter;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public interface AnimalsRepository {
    void save(AnimalsShelter animalsShelter);

    int remove(AnimalsShelter animalsShelter);

    String removeOne(AnimalsShelter animalsShelter);

    Map<Animals, Integer> getAll();
}
