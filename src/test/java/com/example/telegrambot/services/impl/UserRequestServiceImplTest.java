package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.Report;
import com.example.telegrambot.repository.*;
import com.example.telegrambot.services.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRequestServiceImplTest {

    private static final String CORRECT_USER_NAME = "Nick";
    private static final long CORRECT_USER_ID = 123456789;
    private static final UserType CORRECT_USER_TYPE = UserType.DEFAULT;
    private static final UserStatus CORRECT_USER_STATUS = UserStatus.APPROVE;

    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private UserRequestServiceImpl userRequestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCheckReportFalse() {
        Update update = new Update();

        boolean result = userRequestService.checkReport(update);

        assertFalse(result);
    }

    @Test
    void testCheckReportTrue() throws URISyntaxException, IOException {

        Long chatId = 1L;
        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);
        when(userRepository.findByTelegramId(1L)).thenReturn(user);

        userRequestService.reportStateByChatId.put(chatId, UserType.DEFAULT);
    }

    @Test
    void testCheckAdopterFalse() {
        Update update = new Update();

        boolean result = userRequestService.checkAdopter(update);

        assertFalse(result);
    }

    @Test
    void testCheckAdopterTrue() {
        Update update = new Update();
        Long chatId = 1L;

        userRequestService.adopterStateByChatId.put(chatId, UserType.DEFAULT);

        boolean result = userRequestService.checkAdopter(update);

        assertFalse(result);
    }

    @Test
    void testCheckUserInGuestCatFalse() {
        Update update = new Update();

        boolean result = userRequestService.checkUserInGuestCat(update);

        assertFalse(result);
    }

    @Test
    void testCheckUserInGuestCatTrue() throws URISyntaxException, IOException {
        Update update = new Update();
        Long chatId = 1L;

        userRequestService.userCatStateByChatId.put(chatId, UserType.DEFAULT);

        boolean result = userRequestService.checkUserInGuestCat(update);

        assertFalse(result);
    }

    @Test
    void testCheckUserInGuestDogFalse() {
        Update update = new Update();

        boolean result = userRequestService.checkUserInGuestDog(update);

        assertFalse(result);
    }

    @Test
    void testCheckUserInGuestDogTrue() {
        Update update = new Update();
        Long chatId = 1L;

        userRequestService.userDogStateByChatId.put(chatId, UserType.DEFAULT);

        boolean result = userRequestService.checkUserInGuestDog(update);

        assertFalse(result);
    }

    @Test
    void testHandleAdopterReport() throws URISyntaxException, IOException {

        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);
        Report report = new Report(user, LocalDate.now(), StatusReport.DEFAULT, "report text", new byte[0]);
        when(userRepository.findByTelegramId(1L)).thenReturn(user);
        when(reportRepository.findReportByUserId(user)).thenReturn(report);

    }

    @Test
    void testSendMessageStartDefaultUser() throws URISyntaxException, IOException {

        com.example.telegrambot.model.User user = null;
        when(userService.findUserByTelegramId(1L)).thenReturn(user);

    }

    @Test
    void testSendMessageStartNotNewUser() {

        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);
        when(userService.findUserByTelegramId(1L)).thenReturn(user);

    }

    @Test
    void testSendMessageStartGuest() {

        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);
        when(userService.findUserByTelegramId(1L)).thenReturn(user);

    }

    @Test
    void testSendMessageStartAdopter() {

        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);
        Long chatId = 1L;

        Report report = new Report(user, LocalDate.now(), StatusReport.DEFAULT, "report text", new byte[0]);
        when(userService.findUserByTelegramId(1L)).thenReturn(user);
        when(reportRepository.findReportByUserId(user)).thenReturn(report);
        SendMessage sendMessage = new SendMessage(chatId, "Спасибо за отчёт! К сожалению, он не полный, " +
                "поэтому мы настоятельно рекомендуем прислать полный отчет, чтобы избежать последствий!");
    }

    @Test
    void testSendMessageStartVolunteer() {
        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);

        when(userService.findUserByTelegramId(1L)).thenReturn(user);


    }

    @Test
    void testSendMessageStartBlockedUser() {

        com.example.telegrambot.model.User user = new com.example.telegrambot.model.User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE,
                CORRECT_USER_STATUS);

        when(userService.findUserByTelegramId(1L)).thenReturn(user);

    }

    @Test
    public void testCreateButtonClick() {
        // Setup
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1234L);
        when(callbackQuery.data()).thenReturn("CLICK_CAT_SHELTER");

        userRequestService.createButtonClick(update);
    }

}