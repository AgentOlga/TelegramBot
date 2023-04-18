package com.example.telegrambot.services.impl;

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
    public User addUser(long userId, String nickName) {

        if (nickName == null) {
            nickName = "Пользователь без ника";
        }
        User newUser = new User(userId, nickName);
        if (userRepository.findAllByUserId(userId)) {
            saveUser(newUser);
            return newUser;
        } else {
            editNickName(userId, nickName);
            return newUser;
        }
    }

    @Override
    public User saveUser(User user) {
        if (!validationService.validate(user)) {
            throw new ValidationException(user.toString());
        }
        return userRepository.save(user);
    }

    @Override
    public Integer countAppeal(User user) {
        return null;
    }

    @Override
    public User editNickName(long userId, String nickName) {
        return null;
    }

    @Override
    public Collection<User> getAllUser() {
        return null;
    }
}
