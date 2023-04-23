package com.example.telegrambot.service;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.User;

import java.util.Collection;

/**
 * Сервис по работе с пользователями телеграм бота.
 */

public interface UserService {

    /**
     * Создание пользователя бота
     * @param userId идентификатор в телеграмме
     * @param nickName ник в телеграмме
     * @param userType тип пользователя
     * @param userStatus статус пользователя
     * @return созданный пользователь бота
     */
    User addUser(long userId,
                 String nickName,
                 UserType userType,
                 UserStatus userStatus);

    /**
     * Создание гостя
     * @param userId идентификатор в телеграмме
     * @param nickName ник в телеграмме
     * @param userType тип пользователя
     * @param userStatus статус пользователя
     * @param firstName имя гостя
     * @param lastName фамилия гостя
     * @param phoneNumber телефон гостя
     * @param carNumber номер машины
     * @return boolean
     */
    User addGuest(long userId,
                  String nickName,
                  UserType userType,
                  ShelterType shelterType,
                  UserStatus userStatus,
                  String firstName,
                  String lastName,
                  String phoneNumber,
                  String carNumber);

    /**
     * Создание усыновителя
     * @param userId идентификатор в телеграмме
     * @param nickName ник в телеграмме
     * @param userType тип пользователя
     * @param userStatus статус пользователя
     * @param firstName имя усыновителя
     * @param lastName фамилия усыновителя
     * @param phoneNumber телефон усыновителя
     * @param carNumber номер машины
     * @param email эл.почта усыновителя
     * @param address адрес усыновителя
     * @return boolean
     */
    User addAdopterOrVolunteer(long userId,
                               String nickName,
                               UserType userType,
                               ShelterType shelterType,
                               UserStatus userStatus,
                               String firstName,
                               String lastName,
                               String phoneNumber,
                               String carNumber,
                               String email,
                               String address);

    /**
     * Сохраняем пользователя
     * @param user новый пользователь
     * @return сохраненный пользователь
     */
    User saveUser(User user);

    /**
     * Выводим всех сохраненных пользователей
     * @return список пользователей
     */
    Collection<User> getAllUser();


}


