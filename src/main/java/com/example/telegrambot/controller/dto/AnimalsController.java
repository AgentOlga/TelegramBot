package com.example.telegrambot.controller.dto;
import com.example.telegrambot.model.Animals;
import com.example.telegrambot.model.AnimalsShelter;
import com.example.telegrambot.services.animalsService.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/Animals")
@Tag(name = "Animals")
@RequiredArgsConstructor
public class AnimalsController {
private final AnimalService animalService;

    /**
     * метод который добавляет животного
     * @param animalsShelter
     * @return
     */
    @Operation(
            summary = "сохранения животного в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "животное сохранено",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Animals.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "не удалось сохранить животное"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ResponseDTO> createAnimals(@RequestBody AnimalsShelter animalsShelter) {
        animalService.addAnimal(animalsShelter);
        return ResponseEntity.ok(new ResponseDTO("животное успешно сохранено"));
    }

    /**
     * метод который выводит список животных
     * @return
     */
    @Operation(
            summary = "Вывести список животных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список животных выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Animals.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если животных нет в списке"
                    )
            }
    )
    @GetMapping ("allAnimals")
    public ResponseEntity<Map<Animals, Integer>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    /**
     * метод удаления животногог из списка
     *
     * @param animalsShelter
     * @return
     */
    @Operation(
            summary = "удалить животное",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "животное удалено",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Animals.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "не получилось удалить животное"
                    )
            }
    )
    @DeleteMapping("{delete}")
    public ResponseEntity<ResponseDTO> deleteAnimals(@RequestBody AnimalsShelter animalsShelter) {
        String deleteAnimal = animalService.deleteAnimal(animalsShelter);
        return ResponseEntity.ok(new ResponseDTO(deleteAnimal + " удален(a) из БД"));
    }
}