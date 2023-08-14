package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.exception.NotFoundUserException;
import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.ValidationService;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final ValidationService validationService = mock(ValidationService.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl userService = new UserServiceImpl(validationService, userRepository);

    @Test
    void findUserByTelegramId_returnUser() {
        User expectedUser = new User(12345L, "testUser", UserType.GUEST, UserStatus.APPROVE);
        when(userRepository.findByTelegramId(12345L)).thenReturn(expectedUser);

        User actualUser = userService.findUserByTelegramId(12345L);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void addUser_newUser_returnNewUser() {
        User expectedUser = new User(12345L, "testUser", UserType.GUEST, UserStatus.APPROVE);
        when(userRepository.findByTelegramId(12345L)).thenReturn(null);
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        when(validationService.validate(expectedUser)).thenReturn(true);

        User actualUser = userService.addUser(12345L, "testUser", UserType.GUEST, UserStatus.APPROVE);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void addUser_existingUser_returnExistingUser() {
        User expectedUser = new User(12345L, "testUser", UserType.GUEST, UserStatus.APPROVE);
        when(userRepository.findByTelegramId(12345L)).thenReturn(expectedUser);

        User actualUser = userService.addUser(12345L, "testUser", UserType.GUEST, UserStatus.APPROVE);

        assertEquals(expectedUser, actualUser);
        verify(userRepository, never()).save(expectedUser);
    }

    @Test
    void addGuest_userNotFound_throwNotFoundUserException() {
        when(userRepository.findByTelegramId(12345L)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> userService.addGuest(12345L, "testUser", UserType.GUEST, ShelterType.DOG_SHELTER, UserStatus.APPROVE,
                        "John", "Doe", "+123456789", "ABC123"));
    }

    @Test
    void addGuest_userFound_updateUserAndReturnNewGuest() {
        User existingUser = new User(12345L, "testUser", UserType.GUEST, UserStatus.APPROVE);
        when(userRepository.findByTelegramId(12345L)).thenReturn(existingUser);

        User expectedGuest = new User(12345L, "testUser", "John", "Doe", "71234567898", "ABC123",
                ShelterType.DOG_SHELTER, UserType.GUEST, UserStatus.APPROVE);


        User actualGuest = userService.addGuest(12345L, "testUser", UserType.GUEST, ShelterType.DOG_SHELTER, UserStatus.APPROVE,
                "John", "Doe", "71234567898", "ABC123");

        assertEquals(expectedGuest, actualGuest);
    }

    @Test
    void addAdopterOrVolunteer_userNotFound_throwNotFoundUserException() {
        when(userRepository.findByTelegramId(12345L)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> userService.addAdopterOrVolunteer(12345L, "testUser", UserType.ADOPTER, ShelterType.DOG_SHELTER,
                        UserStatus.APPROVE, "John", "Doe", "+123456789",
                        "ABC123", "johndoe@gmail.com", "123 Main St."));
    }

//    @Test
//    void addAdopterOrVolunteer_userFound_updateGuestAndReturnNewAdopterOrVolunteer() {
//        User existingGuest = new User(12345L, "testUser", "John", "Doe", "71234567898", "ABC123",
//                ShelterType.DOG_SHELTER, UserType.GUEST, UserStatus.APPROVE);
//        when(userRepository.findByTelegramId(12345L)).thenReturn(existingGuest);
//
//        User expectedAdopterOrVolunteer = new User(12345L, "testUser", "John", "Doe", "71234567898", "ABC123",
//                "johndoe@gmail.com", "123 Main St.", ShelterType.CAT_SHELTER, UserType.ADOPTER, UserStatus.APPROVE);
//
//
//        User actualAdopterOrVolunteer = userService.addAdopterOrVolunteer(12345L, "testUser", UserType.ADOPTER,
//                ShelterType.CAT_SHELTER, UserStatus.APPROVE, "John", "Doe", "71234567898",
//                "ABC123", "johndoe@gmail.com", "123 Main St.");
//
//        assertEquals(expectedAdopterOrVolunteer, actualAdopterOrVolunteer);
//    }

    @Test
    void saveUser_validUser_returnSavedUser() {
        User expectedUser = new User(12345L, "testUser", UserType.GUEST, UserStatus.APPROVE);
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        when(validationService.validate(expectedUser)).thenReturn(true);

        User actualUser = userService.saveUser(expectedUser);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void saveUser_invalidUser_throwValidationException() {
        User invalidUser = new User(12345L, "", UserType.DEFAULT, UserStatus.BLOCKED);
        when(validationService.validate(invalidUser)).thenReturn(false);

        assertThrows(ValidationException.class, () -> userService.saveUser(invalidUser));
    }

    @Test
    void getAllUser_returnAllUsers() {
        Collection<User> expectedUsers = List.of(
                new User(1L, "user1", UserType.GUEST, UserStatus.APPROVE),
                new User(2L, "user2", UserType.ADOPTER, UserStatus.APPROVE)
        );
        when(userRepository.findAll()).thenReturn((List<User>) expectedUsers);

        Collection<User> actualUsers = userService.getAllUser();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void updateStatusUserById_updateUserStatus() {
        Long userId = 12345L;
        UserStatus newUserStatus = UserStatus.BLOCKED;

        userService.updateStatusUserById(userId, newUserStatus);

        verify(userRepository).updateStatusUserById(userId, newUserStatus);
    }

}