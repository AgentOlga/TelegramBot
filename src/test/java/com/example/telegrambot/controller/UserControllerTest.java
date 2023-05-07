package com.example.telegrambot.controller;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.model.User;
import com.example.telegrambot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {

    private static final String CORRECT_USER_NAME = "Nick";
    private static final String CORRECT_USER_NAME_TWO = "Bob";
    private static final long CORRECT_USER_ID = 123456789;
    private static final long CORRECT_USER_ID_TWO = 987654321;
    private static final UserType CORRECT_USER_TYPE = UserType.DEFAULT;
    private static final UserType CORRECT_USER_TYPE_TWO = UserType.GUEST;
    private static final UserStatus CORRECT_USER_STATUS = UserStatus.APPROVE;
    private static final String CORRECT_FIRST_NAME = "Ivan";
    private static final String CORRECT_LAST_NAME = "Ivanov";
    private static final String CORRECT_PHONE_NUMBER = "1234567891";
    private static final String CORRECT_CAR_NUMBER = "123456";
    private static final ShelterType CORRECT_SHELTER_TYPE = ShelterType.DOG_SHELTER;
    private static final String CORRECT_MAIL = "Ivan@mail.ru";
    private static final String CORRECT_ADDRESS = "Astana, lenina 12";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void addNewUser_withValidParameters_returnsOkResponse() {

        User userMock = mock(User.class);
        when(userService
                .addUser(anyLong(), anyString(), any(UserType.class), any(UserStatus.class)))
                .thenReturn(userMock);

        ResponseEntity<User> response = userController
                .addNewUser(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userMock, response.getBody());
    }

    @Test
    void addNewUser_withInvalidParameters_returnsNotFoundResponse() {

        when(userService
                .addUser(anyLong(), anyString(), any(UserType.class), any(UserStatus.class)))
                .thenThrow(new RuntimeException());

        ResponseEntity<User> response = userController
                .addNewUser(CORRECT_USER_ID, null, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testAddNewGuest_Success() {
        User userMock = mock(User.class);
        when(userService
                .addGuest(anyLong(),
                        anyString(),
                        any(UserType.class),
                        any(ShelterType.class),
                        any(UserStatus.class),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()))
                .thenReturn(userMock);

        ResponseEntity<User> response = userController
                .addNewGuest(CORRECT_USER_ID,
                        CORRECT_USER_NAME,
                        CORRECT_FIRST_NAME,
                        CORRECT_LAST_NAME,
                        CORRECT_PHONE_NUMBER,
                        CORRECT_CAR_NUMBER,
                        CORRECT_SHELTER_TYPE,
                        CORRECT_USER_TYPE,
                        CORRECT_USER_STATUS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userMock, response.getBody());
    }

    @Test
    public void testAddNewAdopterOrVolunteer_Success() throws Exception {
        User userMock = mock(User.class);
        when(userService
                .addAdopterOrVolunteer(anyLong(),
                        anyString(),
                        any(UserType.class),
                        any(ShelterType.class),
                        any(UserStatus.class),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()))
                .thenReturn(userMock);

        ResponseEntity<User> response = userController
                .addNewAdopterOrVolunteer(CORRECT_USER_ID,
                        CORRECT_USER_NAME,
                        CORRECT_FIRST_NAME,
                        CORRECT_LAST_NAME,
                        CORRECT_PHONE_NUMBER,
                        CORRECT_CAR_NUMBER,
                        CORRECT_ADDRESS,
                        CORRECT_MAIL,
                        CORRECT_SHELTER_TYPE,
                        CORRECT_USER_TYPE,
                        CORRECT_USER_STATUS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userMock, response.getBody());
    }

    @Test
    public void testGetAllAdopter_Success() throws Exception {
        User user1 = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE, CORRECT_USER_STATUS);
        User user2 = new User(CORRECT_USER_ID_TWO, CORRECT_USER_NAME_TWO, CORRECT_USER_TYPE_TWO, CORRECT_USER_STATUS);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userService.getAllUser()).thenReturn(users);

        ResponseEntity<Collection<User>> response = userController.getAllAdopter();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}