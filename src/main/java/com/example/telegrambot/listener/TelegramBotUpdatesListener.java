package com.example.telegrambot.listener;


import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.model.Report;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.ReportRepository;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.ReportService;
import com.example.telegrambot.services.UserRequestService;

import com.example.telegrambot.services.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;

import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Объект, уведомляемый о событии.
 * Он должен быть зарегистрирован источником событий
 * и реализовывать методы для получения и обработки уведомлений.
 */
@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static String textReport;
    private static byte[] picture;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final Pattern pattern = Pattern
            .compile("(^[А-я]+)\\s+([А-я]+)\\s+(\\d{10})\\s+([А-я0-9\\d]+$)");//ALT+Enter -> check
    private final UserRequestService userRequestService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(UserService userService, UserRepository userRepository, ReportService reportService, ReportRepository reportRepository, UserRequestService userRequestService,
                                      TelegramBot telegramBot) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.reportService = reportService;
        this.reportRepository = reportRepository;

        this.userRequestService = userRequestService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Handles update: {}", update);

            if (userRequestService.checkReport(update))
                return;

            if (update.message() == null) {
                //handle button click
                userRequestService.createButtonClick(update);
            } else {
                //handle user input
                userRequestService.sendMessageStart(update);
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
