package com.example.telegrambot.services;

import com.example.telegrambot.model.User;

import java.util.Collection;

/**
 * Сервис по работе с пользователями телеграм бота.
 */
public interface UserService {

    /**
     * Создание пользователя
     * @param userId идентификатор
     * @param nickName ник пользователя
     * @return созданный пользователь
     */
    User addUser(long userId, String nickName);

    /**
     * Сохраняем пользователя
     * @param user новый пользователь
     * @return сохраненный пользователь
     */
    User saveUser(User user);

    /**
     * Считаем количество обращений пользователя к боту
     * @param user пользователь
     * @return количество обращений
     */
    Integer countAppeal(User user);

    /**
     * Изменяем ник пользователя, если он поменялся у него
     * @param userId идентификатор
     * @param nickName новый ник пользователя
     * @return пользователь с измененным ником
     */
    User editNickName(long userId, String nickName);

    /**
     * Выводим всех сохраненных пользователей
     * @return список пользователей
     */
    Collection<User> getAllUser();


}
