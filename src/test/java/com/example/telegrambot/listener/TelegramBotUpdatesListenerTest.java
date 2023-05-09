package com.example.telegrambot.listener;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.Dialog;
import com.example.telegrambot.model.Report;
import com.example.telegrambot.model.User;
import com.example.telegrambot.services.UserRequestService;
import com.example.telegrambot.services.UserService;
import com.example.telegrambot.services.impl.UserRequestServiceImpl;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.telegrambot.constants.ConstantValue.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {

    @Test
    void testProcess() {
        UserRequestService userRequestServiceMock = mock(UserRequestService.class);
        Logger loggerMock = mock(Logger.class);
        TelegramBot telegramBotMock = mock(TelegramBot.class);
        UpdatesListener updatesListener = new TelegramBotUpdatesListener(userRequestServiceMock, telegramBotMock);
        List<Update> updates = new ArrayList<>();

        Update update1 = new Update();
        Message message1 = new Message();
        message1.text();
        update1.message();
        updates.add(update1);

        Update update2 = new Update();
        InlineQuery inlineQuery = new InlineQuery();
        inlineQuery.query();
        update2.inlineQuery();
        updates.add(update2);

        when(userRequestServiceMock.checkReport(update1)).thenReturn(false);
        when(userRequestServiceMock.checkAdopter(update1)).thenReturn(false);
        when(userRequestServiceMock.checkUserInGuestCat(update1)).thenReturn(false);
        when(userRequestServiceMock.checkUserInGuestDog(update1)).thenReturn(false);

        when(userRequestServiceMock.checkReport(update2)).thenReturn(false);
        when(userRequestServiceMock.checkAdopter(update2)).thenReturn(false);
        when(userRequestServiceMock.checkUserInGuestCat(update2)).thenReturn(false);
        when(userRequestServiceMock.checkUserInGuestDog(update2)).thenReturn(false);

        updatesListener.process(updates);

        verify(userRequestServiceMock, times(2)).checkReport(any(Update.class));
        verify(userRequestServiceMock, times(2)).checkAdopter(any(Update.class));
        verify(userRequestServiceMock, times(2)).checkUserInGuestCat(any(Update.class));
        verify(userRequestServiceMock, times(2)).checkUserInGuestDog(any(Update.class));

        verify(loggerMock, times(0)).error(any(String.class), any(Exception.class));
    }




//    private static final String CORRECT_USER_NAME = "Nick";
//    private static final long CORRECT_USER_ID = 123456789;
//    private static final UserType CORRECT_USER_TYPE = UserType.DEFAULT;
//    private static final UserStatus CORRECT_USER_STATUS = UserStatus.APPROVE;
//    private static final String CORRECT_FIRST_NAME = "Ivan";
//    private static final String INCORRECT_FIRST_NAME = "ivan";
//    private static final String CORRECT_LAST_NAME = "Ivanov";
//    private static final String CORRECT_PHONE_NUMBER = "1234567891";
//    private static final String INCORRECT_PHONE_NUMBER = "12345678910";
//    private static final String CORRECT_CAR_NUMBER = "123456";
//    private static final ShelterType CORRECT_SHELTER_TYPE = ShelterType.DOG_SHELTER;
//    private static final String CORRECT_MAIL = "Ivan@mail.ru";
//    private static final String CORRECT_ADDRESS = "Astana, lenina 12";
//    @Test
//    public void testSendMessageStart() { // Mock objects
//        Update update = mock(Update.class);
//        Message message = mock(Message.class);
//
//
//        // Set up message object
//        when(update.message()).thenReturn(message);
//
//        when(message.text()).thenReturn("/start");
//
//        User user = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE, CORRECT_USER_STATUS);
//        // Set up user object
//        when(user.getTelegramId()).thenReturn(CORRECT_USER_ID);
//        when(user.getFirstName()).thenReturn("John");
//        when(user.getTelegramNick()).thenReturn(CORRECT_USER_NAME);
//        when(userService.findUserByTelegramId(CORRECT_USER_ID)).thenReturn(user);
//        when(user.getUserType()).thenReturn(UserType.DEFAULT);
//        when(user.getUserStatus()).thenReturn(UserStatus.APPROVE);
//
//// Call the method
//        userRequestService.sendMessageStart(update);
//
//        // Verify that the correct methods were called
//        verify(userService).findUserByTelegramId(CORRECT_USER_ID);
//        verify(userRequestServiceImpl).greetingNewUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString());
//        verify(userService).addUser(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_USER_TYPE, CORRECT_USER_STATUS);
//    }
////    private final UserRequestService userRequestService;
////
////    TelegramBotUpdatesListenerTest(UserRequestService userRequestService) {
////        this.userRequestService = userRequestService;
////    }
//
//    //    @Test
////    public void startCommandTest() throws URISyntaxException, IOException {
////        String json = Files.readString(
////                Path.of(TelegramBotUpdatesListener.class.getResource("update.json").toURI()));
////        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);
////        SendMessage actualMessage = userRequestService.sendMessageStart(update);
////        assertThat(actualMessage).isNotNull();
////        assertThat(actualMessage)
////                .isEqualTo(messageService.sendReplyMessage(12345, messageToSend, messageService.generateMenuKeyBoard("\uD83D\uDE3A CAT SHELTER", "\uD83D\uDC36 DOG SHELTER", INFO)));
////    }
//    @MockBean
//    private TelegramBot telegramBot;
//    @MockBean
//    private UserRequestService userRequestService;
//    @MockBean
//    private UserRequestServiceImpl userRequestServiceImpl;
//    @MockBean
//    private UserService userService;
//    @Autowired
//    private TelegramBotUpdatesListener telegramBotUpdatesListener;
//
//    @Test
//    public void handleStartCommandNewGuestTest() throws URISyntaxException, IOException {
//        String json = Files.readString(
//                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
//        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);
//        telegramBotUpdatesListener.process(Collections.singletonList(update));
//
//        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
//        verify(telegramBot).execute(argumentCaptor.capture());
//        SendMessage actual = argumentCaptor.getValue();
//
//        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123);
//        Assertions.assertThat(actual.getParameters().get("text"))
//                .isEqualTo(String.format(GREETINGS_AT_THE_PET_SHELTER, update.message().from().username()));
//    }
//
//    @Test
//    public void handleStartTest() throws URISyntaxException, IOException {
//
//        String json = Files.readString(
//                Path.of(TelegramBotUpdatesListener.class.getResource("update.json").toURI()));
//        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);
//        SendResponse sendResponse = BotUtils.fromJson("""
//                {
//                 "ok": true
//                }""", SendResponse.class);
//
//        when(telegramBot.execute(any())).thenReturn(sendResponse);
//
//        userRequestService.sendMessageStart(update);
//
//        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
//        verify(telegramBot).execute(argumentCaptor.capture());
//        SendMessage actual = argumentCaptor.getValue();
//
//        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
//        Assertions.assertThat(actual.getParameters().get("text"))
//                .isEqualTo(String.format(GREETINGS_AT_THE_PET_SHELTER, update.message().from().username()));
//    }
//
//    @Test
//    public void handleCatShelterSelectTest() throws URISyntaxException, IOException {
//        String json = Files.readString(
//                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
//        Update update = BotUtils.fromJson(json.replace("%text%", CLICK_CAT_SHELTER), Update.class);
//        SendResponse sendResponse = BotUtils.fromJson("""
//                {
//                 "ok": true
//                }""", SendResponse.class);
//
//        when(telegramBot.execute(any())).thenReturn(sendResponse);
//        telegramBotUpdatesListener.process(Collections.singletonList(update));
//
//        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
//        verify(telegramBot, Mockito.times(2)).execute(argumentCaptor.capture());
//        SendMessage actual = argumentCaptor.getValue();
//
//        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123);
//        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(GREETINGS_AT_THE_CAT_SHELTER);
//    }
//
//    @Test
//    public void handleDogShelterSelectTest() throws URISyntaxException, IOException {
//        String json = Files.readString(
//                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("update.json").toURI()));
//        Update update = BotUtils.fromJson(json.replace("%text%", CLICK_DOG_SHELTER), Update.class);
//        SendResponse sendResponse = BotUtils.fromJson("""
//                {
//                 "ok": true
//                }""", SendResponse.class);
//
//        when(telegramBot.execute(any())).thenReturn(sendResponse);
//
//        telegramBotUpdatesListener.process(Collections.singletonList(update));
//
//        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
//        verify(telegramBot, Mockito.times(2)).execute(argumentCaptor.capture());
//        SendMessage actual = argumentCaptor.getValue();
//
//        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123);
//        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(GREETINGS_AT_THE_DOG_SHELTER);
//    }

}