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
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Бизнес-логика по работе с пользователями
 */
@Service
public class UserServiceImpl implements UserService {

    private final ValidationService validationService;

    private final UserRepository userRepository;

    public UserServiceImpl(ValidationService validationService, UserRepository userRepository) {
        this.validationService = validationService;
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(long userId, String nickName, UserType userType, UserStatus userStatus) {

        User newUser = new User(userId, nickName, userType, userStatus);
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            saveUser(newUser);
            return newUser;
        }
        return user;
    }

    @Override
    public User addGuest(long userId,
                            String nickName,
                            UserType userType,
                            ShelterType shelterType,
                            UserStatus userStatus,
                            String firstName,
                            String lastName,
                            String phoneNumber,
                            String carNumber) {

        User newGuest = new User(userId,
                nickName,
                firstName,
                lastName,
                phoneNumber,
                carNumber,
                shelterType,
                userType,
                userStatus);
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new NotFoundUserException(toString());
        }
        userRepository.updateUserInGuestById(userId,
                firstName,
                lastName,
                phoneNumber,
                carNumber,
                shelterType,
                userType,
                userStatus);

        return newGuest;
    }

    @Override
    public User addAdopterOrVolunteer(long userId,
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

        User newAdopterOrVolunteer = new User(userId,
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
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new NotFoundUserException(toString());
        }
        userRepository.updateGuestInAdopterById(userId,
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
}
