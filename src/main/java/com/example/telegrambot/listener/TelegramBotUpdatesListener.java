package com.example.telegrambot.listener;


import com.example.telegrambot.services.UserRequestService;
import com.example.telegrambot.services.impl.UserRequestServiceImpl;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;


import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Объект, уведомляемый о событии.
 * Он должен быть зарегистрирован источником событий
 * и реализовывать методы для получения и обработки уведомлений.
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final UserRequestService userRequestService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final UserRequestServiceImpl userRequestServiceImpl;

    public TelegramBotUpdatesListener(UserRequestService userRequestService,
                                      TelegramBot telegramBot, UserRequestServiceImpl userRequestServiceImpl) {
        this.userRequestService = userRequestService;
        this.telegramBot = telegramBot;
        this.userRequestServiceImpl = userRequestServiceImpl;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {

            updates.forEach(update -> {
                        logger.info("Handles update: {}", update);

                if (update.message() != null) {
                    userRequestService.sendMessageStart(update);

                } else {

                    userRequestService.createButtonClick(update);
                }

                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
