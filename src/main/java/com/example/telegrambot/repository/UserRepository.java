package com.example.telegrambot.repository;
import com.example.telegrambot.model.User;
import java.util.Collection;

public interface UserRepository {

    User findUserByChatId(Long chatId);
    Collection<User> findUserByShelter(String shelter);

}
