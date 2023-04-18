package com.example.telegrambot.controller;

import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.services.AdopterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("pet-shelter/adopter")
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
                    "(имя, фамилия, номер телефона, номер машины (если есть)," +
                    "эл.почту, адрес, приют, статус)"
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

    @PutMapping
    @Operation(
            summary = "Изменения данных усыновителя",
            description = "Нужно написать новые данные усыновителя " +
                    "(имя, фамилия, номер телефона, номер машины (если есть)," +
                    "эл.почту, адрес, приют, статус)"
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
    public ResponseEntity<Adopter> updateAdopter(@RequestBody Adopter adopter) {

        try {
            return ResponseEntity.ok(adopterService.updateAdopter(adopter));
        } catch (RuntimeException e) {
            e.getStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    @Operation(
            summary = "Удаление усыновителя",
            description = "Нужно написать данные усыновителя, которого нужно удалить " +
                    "(имя, фамилия, номер телефона, номер машины (если есть)," +
                    "эл.почту, адрес, приют, статус)"
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
    public ResponseEntity<Adopter> deleteAdopter(@RequestBody Adopter adopter) {

        try {
            return ResponseEntity.ok(adopterService.deleteAdopter(adopter));
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
}
