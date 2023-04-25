package com.example.telegrambot;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.service.UserService;
import com.example.telegrambot.controller.UserController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    @Test
    public void getAllOrdersPositiveTest() throws Exception{

        final Long userId = 232L;
        final String telegramNick = "DanishFamously";
        final String shelterType = "DOG";
        final String firstName = "Olga";
        final String lastName ="Philippova";
        final String phoneNumber = "userPhone";
        final String email = "userMail";
        final String carNumber= "12 34";
        final String address ="Copenhagen";

        User user = new User(userId, telegramNick, firstName, lastName, phoneNumber, email, carNumber, address, ShelterType.DOG_SHELTER,UserType.DEFAULT , UserStatus.APPROVE);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/all_orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].shelterType").value(shelterType))
                .andExpect(jsonPath("$[0].firstName").value(firstName))
                .andExpect(jsonPath("$[0].phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$[0].email").value(email));



    }

    @Test
    public void getAllOrdersTestWhenReturnEmptyList() throws Exception{

        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/all_orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserByShelterPositiveTest() throws Exception{

        final Long userId = 232L;
        final String telegramNick = "DanishFamously";
        final String shelterType = "DOG";
        final String firstName = "Olga";
        final String lastName ="Philippova";
        final String phoneNumber = "userPhone";
        final String email = "userMail";
        final String carNumber= "12 34";
        final String address ="Copenhagen";

        User user = new User(userId, telegramNick, firstName, lastName, phoneNumber, email, carNumber, address, shelterType, User user, );
        List<User> users = new ArrayList<>(List.of(user));

        Mockito.when(userRepository.findUserByShelter(shelterType)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/shelter?shelter={shelter}", shelterType)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].shelterType").value(shelterType))
                .andExpect(jsonPath("$[0].firstname").value(firstName))
                .andExpect(jsonPath("$[0].phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$[0].email").value(email));

    }

    @Test
    public void findUserByShelterTestWhenReturnEmptyList() throws Exception{

        final String shelter = "DOG";
        Mockito.when(userRepository.findUserByShelter(shelter)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/shelter?shelter={shelter}", shelter)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}

