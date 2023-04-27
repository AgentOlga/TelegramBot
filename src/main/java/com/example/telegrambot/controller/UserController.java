package com.example.telegrambot.controller;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.User;
import com.example.telegrambot.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер пользователей.
 */
@RestController
@RequestMapping("/pet-shelter")
@Tag(name = "API по работе с пользователями",
        description = "CRUD-операции для работы с пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @Operation(
            summary = "Регистрация пользователя бота",
            description = "Нужно написать данные пользователя " +
                    "(ID телеграмм, ник телеграмм, тип пользователя, статус пользователя)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось добавить пользователя"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<User> addNewUser(@RequestParam(required = false) Long userId,
                                           @RequestParam(required = false) String telegramNick,
                                           @RequestParam(required = false) UserType userType,
                                           @RequestParam(required = false) UserStatus userStatus) {

        try {
            return ResponseEntity.ok(userService.addUser(userId,
                    telegramNick,
                    userType,
                    userStatus));
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/guest")
    @Operation(
            summary = "Регистрация гостя",
            description = "Нужно написать данные гостя " +
                    "(ID телеграмм, ник телеграмм, имя, фамилия, телефон, номер машины, " +
                    "тип пользователя, статус пользователя)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось добавить гостя"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<User> addNewGuest(@RequestParam(required = false) Long userId,
                                            @RequestParam(required = false) String telegramNick,
                                            @RequestParam(required = false) String firstName,
                                            @RequestParam(required = false) String lastName,
                                            @RequestParam(required = false) String phoneNumber,
                                            @RequestParam(required = false) String carNumber,
                                            @RequestParam(required = false) ShelterType shelterType,
                                            @RequestParam(required = false) UserType userType,
                                            @RequestParam(required = false) UserStatus userStatus) {

        try {
            return ResponseEntity.ok(userService.addGuest(userId,
                    telegramNick,
                    userType,
                    shelterType,
                    userStatus,
                    firstName,
                    lastName,
                    phoneNumber,
                    carNumber));
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/adopter_volunteer")
    @Operation(
            summary = "Регистрация усыновителя/волонтера",
            description = "Нужно написать данные усыновителя/волонтера " +
                    "(ID телеграмм, ник телеграмм, имя, фамилия, телефон, номер машины, " +
                    "адрес, эл.почта," +
                    "тип пользователя, статус пользователя)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось добавить усыновителя/волонтера"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<User> addNewAdopterOrVolunteer(@RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) String telegramNick,
                                                         @RequestParam(required = false) String firstName,
                                                         @RequestParam(required = false) String lastName,
                                                         @RequestParam(required = false) String phoneNumber,
                                                         @RequestParam(required = false) String carNumber,
                                                         @RequestParam(required = false) String address,
                                                         @RequestParam(required = false) String email,
                                                         @RequestParam(required = false) ShelterType shelterType,
                                                         @RequestParam(required = false) UserType userType,
                                                         @RequestParam(required = false) UserStatus userStatus) {

        try {
            return ResponseEntity.ok(userService.addAdopterOrVolunteer(userId,
                    telegramNick,
                    userType,
                    shelterType,
                    userStatus,
                    firstName,
                    lastName,
                    phoneNumber,
                    carNumber,
                    email,
                    address));
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Список всех пользователей"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось получить список пользователей"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<Collection<User>> getAllAdopter() {

        try {
            return ResponseEntity.ok(userService.getAllUser());
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
