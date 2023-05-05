package com.example.telegrambot.listener;


import com.example.telegrambot.repository.ReportRepository;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.ReportService;
import com.example.telegrambot.services.UserRequestService;

import com.example.telegrambot.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.model.Update;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Объект, уведомляемый о событии.
 * Он должен быть зарегистрирован источником событий
 * и реализовывать методы для получения и обработки уведомлений.
 */
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final UserRequestService userRequestService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(UserRequestService userRequestService,
                                      TelegramBot telegramBot) {

        this.userRequestService = userRequestService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    @SneakyThrows
    public int process(List<Update> updates) {

        try {

            updates.forEach(update -> {
                logger.info("Handles update: {}", update);

                if (userRequestService.checkReport(update)) {
                    return;
                }

                if (userRequestService.checkUserInGuestCat(update)) {
                    return;
                }

                if (userRequestService.checkUserInGuestDog(update)) {
                    return;
                }

                if (userRequestService.checkFreeMessage(update)) {
                    return;
                }

                if (update.message() == null) {
                    userRequestService.createButtonClick(update);

                } else {
                    userRequestService.sendMessageStart(update);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
