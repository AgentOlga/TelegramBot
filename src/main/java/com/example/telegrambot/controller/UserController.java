package com.example.telegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.telegrambot.model.User;
import com.example.telegrambot.service.UserService;
import java.util.Collection;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Вывести список ззаявок",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список заявок выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,

                                            schema = @Schema(implementation = User.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если заявок нет"
                    )
            }
    )
    @GetMapping("all_orders")
    public ResponseEntity<Collection<User>> getALlOrders() {
        return ResponseEntity.ok(userService.getAllOrders());
    }

    @Operation(
            summary = "Вывести список ззаявок по полученному значению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "список заявок выведен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Если заявок нет"
                    )
            }
    )
    @GetMapping("{shelter}")
    public ResponseEntity<Collection<User>> findUserByShelter(@RequestParam String shelter){
        return ResponseEntity.ok(userService.findUserByShelter(shelter));
    }
}
