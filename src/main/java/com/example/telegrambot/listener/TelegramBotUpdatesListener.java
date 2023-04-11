package com.example.telegrambot.listener;

import com.example.telegrambot.services.InlineKeyboardMarkupService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Объект, уведомляемый о событии.
 * Он должен быть зарегистрирован источником событий
 * и реализовывать методы для получения и обработки уведомлений.
 */
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final InlineKeyboardMarkupService inlineKeyboardMarkupService;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(InlineKeyboardMarkupService inlineKeyboardMarkupService, TelegramBot telegramBot) {
        this.inlineKeyboardMarkupService = inlineKeyboardMarkupService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {

            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        logger.info("Handles update: {}", update);

                        Message message = update.message();
                        Long chatId = message.from().id();
                        String text = message.text();


                        if ("/start".equals(text)) {
                            SendMessage sendMessage = new SendMessage(chatId, "Здравствуйте!\n" +
                                    "Вас приветствует приют домашних животных города Астаны!\n" +
                                    "Выберете, пожалуйста, приют!");
                        /*}
                        if (update.message() == null) {
                            processMessage(update);
                        } else {*/
                            sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
                            telegramBot.execute(sendMessage);
                            processButtonClick(update);
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void processButtonClick(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            long chatId = callbackQuery.message().chat().id();
            switch (callbackQuery.data()) {
                case "button_Cat_Shelter_clicked":
                    // Cat shelter selected
                    sendButtonClickMessage(chatId, "button_Cat_Shelter_clicked");
                    //processCatShelterClick(chatId);
                    break;
                case "button_Dog_Shelter_clicked":
                    // Dog shelter selected
                    sendButtonClickMessage(chatId, "button_Dog_Shelter_clicked");
                    //processDogShelterClick(chatId);
                    break;

            }
        }
    }

    private void sendButtonClickMessage(long chatId, String message) {
        sendMessage(chatId, message);
    }

    /*private void processCatShelterClick(long chatId) {
        shelterType = PetType.CAT;
        saveGuest(chatId, shelterType);
        sendStage0Message(chatId, CAT_SHELTER_WELCOME_MSG_TEXT);
    }

    private void processDogShelterClick(long chatId) {
        shelterType = DOG;
        saveGuest(chatId, shelterType);
        sendStage0Message(chatId, DOG_SHELTER_WELCOME_MSG_TEXT);
    }*/

}
