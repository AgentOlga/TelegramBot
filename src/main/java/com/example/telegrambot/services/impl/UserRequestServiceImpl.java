package com.example.telegrambot.services.impl;

import com.example.telegrambot.listener.TelegramBotUpdatesListener;
import com.example.telegrambot.services.InlineKeyboardMarkupService;
import com.example.telegrambot.services.UserRequestService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.telegrambot.constants.ConstantValue.*;

/**
 * Бизнес-логика по работе запросов от пользователей.
 */
@Service
public class UserRequestServiceImpl implements UserRequestService {

    private final InlineKeyboardMarkupService inlineKeyboardMarkupService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public UserRequestServiceImpl(InlineKeyboardMarkupService inlineKeyboardMarkupService, TelegramBot telegramBot) {
        this.inlineKeyboardMarkupService = inlineKeyboardMarkupService;
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessageStart(Update update) {

        Message message = update.message();
        Long chatId = message.from().id();
        String text = message.text();
        String userName;

        if ("/start".equals(text)) {

            userName = update.message().from().username();
            SendMessage sendMessage =
                    new SendMessage(chatId, String.format(GREETINGS_AT_THE_PET_SHELTER, userName));

            sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
            SendResponse sendResponse = telegramBot.execute(sendMessage);
            if (!sendResponse.isOk()) {
                logger.error("Error during sending message: {}", sendResponse.description());
            }
        }
    }

    @Override
    public void createButtonClick(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            long chatId = callbackQuery.message().chat().id();
            String data = callbackQuery.data();
            switch (data) {
                case CLICK_CAT_SHELTER:

                    processCatShelterClick(chatId);
                    break;
                case CLICK_DOG_SHELTER:

                    processDogShelterClick(chatId);
                    break;

            }
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (sendResponse != null && !sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void processDogShelterClick(long chatId) {
        //shelterType = DOG;
        //saveGuest(chatId, shelterType);

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_DOG_SHELTER);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelter());

        sendMessage(sendMessage);
    }

    private void processCatShelterClick(long chatId) {
//        shelterType = PetType.CAT;
//        saveUser(chatId, shelterType);

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_CAT_SHELTER);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelter());

        sendMessage(sendMessage);
    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

     /*private void saveUser(long chatId, PetType lastMenu) {
        User user = userRepository.findByChatId(chatId);
        if (user == null) {
            guest = new Guest(chatId, new Timestamp(System.currentTimeMillis()), lastMenu);
            guestRepository.save(guest);
        }
    }*/
}