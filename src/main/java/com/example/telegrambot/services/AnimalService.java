package com.example.telegrambot.services;

import com.example.telegrambot.constants.Color;
import com.example.telegrambot.constants.PetType;
import com.example.telegrambot.constants.Sex;
import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animal;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * Сервис по работе с животными.
 */
public interface AnimalService {

    /**
     * Сохраняем новое животное
     * @param nickName кличка
     * @param petType тип животного
     * @param color цвет
     * @param sex пол
     * @return сохраненное животное
     */
    Animal saveAnimal(String nickName,
                      PetType petType,
                      Color color,
                      Sex sex);

    /**
     * Изменяем параметры животного по идентификатору
     *
     * @param id идентификатор животного
     */
    void updateAnimalById(Long id,
                          String nickName,
                          PetType petType,
                          Color color,
                          Sex sex);

    /**
     * Удаляем животное по идентификатору
     *
     * @param id идентификатор животного
     */
    void deleteAnimalById(Long id);

    /**
     * Выводим всех животных
     *
     * @return список животных
     */
    Collection<Animal> getAllAnimal();

}
