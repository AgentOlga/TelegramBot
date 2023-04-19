package com.example.telegrambot.repository;

import com.example.telegrambot.model.Animals;
import com.example.telegrambot.model.AnimalsShelter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AnimalsRepositoryImpl  implements AnimalsRepository{
    private Map<Animals, Integer> animalsMap = new HashMap<>();

    /**
     * метод сохранения животного
     * @param animalsShelter
     */
    @Override
    public void save(AnimalsShelter animalsShelter) {
        Animals animals = animalsShelter.getAnimals();
        if (animalsMap.containsKey(animals)) {
            animalsMap.replace(animals, animalsMap.get(animals));
        } else {
            animalsMap.put(animals, animalsShelter.getQuantity());
        }

    }

    /**
     * метод удаления животного
     *
     * @param animalsShelter
     * @return
     */
    @Override
    public int remove(AnimalsShelter animalsShelter) {
        Animals animals = animalsShelter.getAnimals();
        if (animalsMap.containsKey(animals)) {
            int quantity = animalsMap.get(animals);

            if (quantity > animalsShelter.getQuantity()) {
                animalsMap.replace(animals,quantity - animalsShelter.getQuantity());
                return animalsShelter.getQuantity();
            } else {
                animalsMap.remove(animals);
                return quantity;
            }
        }
        return 0;
    }

    /**
     * метод удаления животного, если он есть в БД
     * @param animalsShelter
     * @return
     */
    @Override
    public String removeOne(AnimalsShelter animalsShelter) {
        String s = "ytn";
        Animals animals = animalsShelter.getAnimals();
        if (animalsMap.containsKey(animals)) {
            animalsMap.remove(animalsShelter);
        }
        return s;
    }


    /**
     * метод который возращает всех животных
     * @return
     */
    @Override
    public Map<Animals, Integer> getAll() {
        return animalsMap;
    }
}
