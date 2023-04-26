package com.example.telegrambot.services;

import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.User;

import java.util.Collection;

/**
 * Сервис по работе с усыновителями.
 */
public interface AdopterService {

    /**
     * Сохраняем нового усыновителя
     * @param adopter новый усыновитель
     * @return сохраненный усыновитель
     */
    Adopter saveAdopter(Adopter adopter);

    /**
     * Изменяем усыновителя
     * @param adopter изменяемый усыновитель
     * @return измененный усыновитель
     */
    Adopter updateAdopter(Adopter adopter);

    /**
     * Удаляем усыновителя
     * @param adopter усыновитель, которого нужно удалить
     * @return удаленный усыновитель
     */
    Adopter deleteAdopter(Adopter adopter);

    /**
     * Выводим всех сохраненных усыновителей
     * @return список усыновителей
     */
    Collection<Adopter> getAllAdopter();

    /**
     * Поиск усыновителя по id
     * @param id идентификатор усыновителя
     * @return найденный усыновитель
     */
    Adopter foundAdopterById(long id);
}
