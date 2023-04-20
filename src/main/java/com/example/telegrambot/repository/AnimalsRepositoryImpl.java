package com.example.telegrambot.repository;

import com.example.telegrambot.exeption.ValidationAmimalExeption;
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
     * @param animalsShelter
     * @return
     */
    @Override
    public String remove(AnimalsShelter animalsShelter) {
        String nickNameAnimal = animalsShelter.getAnimals().getNickName();
        Animals animals = animalsShelter.getAnimals();
        if (animalsMap.containsKey(animals)) {
            animalsMap.remove(animals);
        } else {
            throw new ValidationAmimalExeption("не возможно удалить животное");
        }
        return nickNameAnimal;
    }
    /**
     * метод который если список пустой то передает исключение
     * иначе выводит весь список
     * @return
     */
    @Override
    public Map<Animals, Integer> getAll() {
        if (animalsMap.isEmpty()) {
            throw new ValidationAmimalExeption("животных нет в списке");
        }
        return animalsMap;
    }
}
