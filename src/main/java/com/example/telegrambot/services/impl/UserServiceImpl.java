package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.exception.NotFoundUserException;
import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.UserService;
import com.example.telegrambot.services.ValidationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Бизнес-логика по работе с пользователями
 */
@Component
public class UserServiceImpl implements UserService {

    private final ValidationService validationService;

    private final UserRepository userRepository;

    public UserServiceImpl(ValidationService validationService,
                           UserRepository userRepository) {
        this.validationService = validationService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User findUserByTelegramId(long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User addUser(long telegramId, String nickName, UserType userType, UserStatus userStatus) {

        User newUser = new User(telegramId, nickName, userType, userStatus);
        User user = userRepository.findByTelegramId(telegramId);
        if (user == null) {
            saveUser(newUser);
            return newUser;
        }
        return user;
    }

    @Override
    @Transactional
    public User addGuest(long telegramId,
                         String nickName,
                         UserType userType,
                         ShelterType shelterType,
                         UserStatus userStatus,
                         String firstName,
                         String lastName,
                         String phoneNumber,
                         String carNumber) {

        User newGuest = new User(telegramId,
                nickName,
                firstName,
                lastName,
                phoneNumber,
                carNumber,
                shelterType,
                userType,
                userStatus);
        User user = userRepository.findByTelegramId(telegramId);
        if (user == null) {
            throw new NotFoundUserException("Пользователь не найден!");
        }
        userRepository.updateUserInGuestById(
                firstName,
                lastName,
                phoneNumber,
                carNumber,
                shelterType,
                userType,
                userStatus,
                telegramId);

        return newGuest;
    }

    @Override
    @Transactional
    public User addAdopterOrVolunteer(long telegramId,
                                      String nickName,
                                      UserType userType,
                                      ShelterType shelterType,
                                      UserStatus userStatus,
                                      String firstName,
                                      String lastName,
                                      String phoneNumber,
                                      String carNumber,
                                      String email,
                                      String address) {

        User newAdopterOrVolunteer = new User(telegramId,
                nickName,
                firstName,
                lastName,
                phoneNumber,
                carNumber,
                address,
                email,
                shelterType,
                userType,
                userStatus);
        User user = userRepository.findByTelegramId(telegramId);
        if (user == null) {
            throw new NotFoundUserException("Пользователь не найден!");
        }
        userRepository.updateGuestInAdopterById(telegramId,
                firstName,
                lastName,
                phoneNumber,
                carNumber,
                shelterType,
                userType,
                userStatus,
                email,
                address);

        return newAdopterOrVolunteer;
    }

    @Override
    public User saveUser(User user) {
        if (!validationService.validate(user)) {
            throw new ValidationException(user.toString());
        }
        return userRepository.save(user);
    }

    @Override
    public Collection<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void updateStatusUserById(Long id, UserStatus userStatus) {

        userRepository.updateStatusUserById(id, userStatus);
    }
}
