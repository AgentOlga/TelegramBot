package com.example.telegrambot.controller;

import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.model.User;
import com.example.telegrambot.services.AdopterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер усыновителя.
 */
@RestController
@RequestMapping("/pet-shelter/adopter")
@Tag(name = "API по работе с усыновителями",
        description = "CRUD-операции для работы с усыновителями")
public class AdopterController {

    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    @PostMapping
    @Operation(
            summary = "Регистрация усыновителя",
            description = "Нужно написать данные усыновителя " +
                    "(id пользователя, id животного, id приюта)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось добавить усыновителя"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<Adopter> saveNewAdopter(@RequestBody Adopter adopter) {

        try {
            return ResponseEntity.ok(adopterService.saveAdopter(adopter));
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Изменения данных усыновителя по идентификатору",
            description = "Нужно написать новые данные усыновителя " +
                    "(id пользователя, id животного, id приюта)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось изменить данне усыновителя"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<Void> updateAdopter(@PathVariable Long id,
                                              @RequestParam(required = false) User user,
                                              @RequestParam(required = false) Animal animal,
                                              @RequestParam(required = false) Shelter shelter) {

        try {
            adopterService.updateAdopterById(id, user, animal, shelter);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление усыновителя по идентификатору",
            description = "Нужно написать id усыновителя, которого нужно удалить"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось удалить усыновителя"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<Void> deleteAdopterById(@PathVariable Long id) {

        try {
            adopterService.deleteAdopterById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Список всех усыновителей"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось получить список усыновителей"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<Collection<Adopter>> getAllAdopter() {

        try {
            return ResponseEntity.ok(adopterService.getAllAdopter());
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Поиск усыновителя по id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Удалось получить усыновителя по id"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Параметры запроса отсутствуют или имеют некорректный формат"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Произошла ошибка, не зависящая от вызывающей стороны"
    )
    public ResponseEntity<Adopter> foundAdopterById(@Parameter(description = "id усыновителя") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(adopterService.foundAdopterById(id));
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
