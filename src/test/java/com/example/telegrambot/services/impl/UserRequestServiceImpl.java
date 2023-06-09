package com.example.telegrambot.services.impl;

import com.example.telegrambot.listener.TelegramBotUpdatesListener;
import com.example.telegrambot.services.InlineKeyboardMarkupService;
import com.example.telegrambot.services.ReplyKeyboardMarkupService;
import com.example.telegrambot.services.UserRequestService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static com.example.telegrambot.constants.ConstantValue.*;

/**
 * Бизнес-логика по работе запросов от пользователей.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Service
public class UserRequestServiceImpl implements UserRequestService {

    private final InlineKeyboardMarkupService inlineKeyboardMarkupService;
    private final ReplyKeyboardMarkupService replyKeyboardMarkupService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public UserRequestServiceImpl(InlineKeyboardMarkupService inlineKeyboardMarkupService,
                                  ReplyKeyboardMarkupService replyKeyboardMarkupService,
                                  TelegramBot telegramBot) {
        this.inlineKeyboardMarkupService = inlineKeyboardMarkupService;
        this.replyKeyboardMarkupService = replyKeyboardMarkupService;
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessageStart(Update update) {

        Message message = update.message();
        Long chatId = message.from().id();
        String text = message.text();
        String userName = update.message().from().username();

        getStart(chatId);

        if ("/start".equals(text)) {

            if (userName == null) {
                greetingNullName(chatId);
            } else {
                greetingUser(chatId, userName);
            }
        }
    }

    private void greetingNullName(Long chatId) {

        String name = "";
        SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETINGS_AT_THE_PET_SHELTER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
    private void greetingUser(Long chatId, String name) {

        SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETINGS_AT_THE_PET_SHELTER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
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

                    getCatShelterClick(chatId);

                    break;
                case CLICK_DOG_SHELTER:

                    getDogShelterClick(chatId);

                    break;
                case CLICK_BACK_TO_SHELTER_TYPE:

                    getMainMenuClick(chatId);

                    break;
                case CLICK_CAT_SHELTER_INFO:

                    getCatShelterInfoClick(chatId);

                    break;
                case CLICK_DOG_SHELTER_INFO:

                    getDogShelterInfoClick(chatId);

                    break;
                case CLICK_HOW_TO_ADOPT_A_CAT:

                    getCatShelterTakeClick(chatId);

                    break;
                case CLICK_HOW_TO_ADOPT_A_DOG:

                    getDogShelterTakeClick(chatId);

                    break;
                case CLICK_SEND_A_CAT_REPORT:

                    getCatShelterReportClick(chatId);

                    break;
                case CLICK_SEND_A_DOG_REPORT:

                    getDogShelterReportClick(chatId);

                    break;
                case CLICK_CALL_A_VOLUNTEER:

                    //todo взаимодействие с волонтером

                    break;
                case CLICK_ARRANGEMENT_CAT_HOME:

                    getCatAtHomeClick(chatId);

                    break;
                case CLICK_ARRANGEMENT_DOG_HOME:

                    getDogAtHomeClick(chatId);

                    break;

            }
        }
    }

    private void getDogAtHomeClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogAtHome());

        sendMessage(sendMessage);
    }

    private void getCatAtHomeClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatAtHome());

        sendMessage(sendMessage);
    }

    private void getDogShelterReportClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelterReport());

        sendMessage(sendMessage);
    }

    private void getCatShelterReportClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterReport());

        sendMessage(sendMessage);
    }

    private void getDogShelterTakeClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelterTake());

        sendMessage(sendMessage);
    }

    private void getCatShelterTakeClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterTake());

        sendMessage(sendMessage);
    }

    private void sendMessage(SendMessage sendMessage) {
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (sendResponse != null && !sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void getMainMenuClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_MAIN_MENU);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());

        sendMessage(sendMessage);
    }

    private void getDogShelterClick(long chatId) {

        //сохраняем пользователя в БД приюта для собак

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_DOG_SHELTER);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelter());

        sendMessage(sendMessage);
    }

    private void getCatShelterClick(long chatId) {

        //сохраняем пользователя в БД приюта для кошек

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_CAT_SHELTER);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelter());

        sendMessage(sendMessage);
    }

    private void getCatShelterInfoClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterInfo());

        sendMessage(sendMessage);

    }

    private void getDogShelterInfoClick(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelterInfo());

        sendMessage(sendMessage);
    }

    private void getStart(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, "");
        sendMessage.replyMarkup(replyKeyboardMarkupService.createStart());

        sendMessage(sendMessage);
    }

    private void getMenu(long chatId) {

        SendMessage sendMessage = new SendMessage(chatId, "");
        sendMessage.replyMarkup(replyKeyboardMarkupService.createStart());

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
