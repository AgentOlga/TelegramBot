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

import static com.example.telegrambot.constants.ConstantValue.*;

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
                            SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_PET_SHELTER);
                        /*}
                        if (update.message() == null) {
                            processMessage(update);
                        } else {*/
                            sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
                            telegramBot.execute(sendMessage);
                            //processButtonClick(update);
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



}
