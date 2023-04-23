package com.example.telegrambot.controller;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.service.SheltersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
@RequestMapping("info")
public class SheltersController {

    private final SheltersService sheltersService;

    public SheltersController(SheltersService sheltersService) {
        this.sheltersService = sheltersService;
    }

    @Operation(
            summary = "Обновление информации в базе данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "информация обновлена",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Shelter.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если информации нет в базе данных"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "обновление информации",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = Shelter.class)
                            )
                    }
            )
    )
    @PutMapping
    public ResponseEntity<Shelter> editInfo(@RequestBody Shelter shelter) {
        Shelter editInfo = sheltersService.editInfo(shelter);
        return ResponseEntity.ok(editInfo);
    }

    @Operation(
            summary = "Вывести список информации",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список информации выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Shelter.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если информации нет"
                    )
            }
    )
    @GetMapping("all_info")

    public ResponseEntity<Collection<Shelter>> getAllInfo() {
        return ResponseEntity.ok((Collection<Shelter>) sheltersService.getAllInfo());
    }
}
