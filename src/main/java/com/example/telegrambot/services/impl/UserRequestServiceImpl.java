package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.listener.TelegramBotUpdatesListener;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.AdopterRepository;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.InlineKeyboardMarkupService;
import com.example.telegrambot.services.ReplyKeyboardMarkupService;
import com.example.telegrambot.services.UserRequestService;
import com.example.telegrambot.services.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.telegrambot.constants.ConstantValue.*;

/**
 * Бизнес-логика по работе запросов от пользователей.
 */
@Component
public class UserRequestServiceImpl implements UserRequestService {

    private final Pattern patternName = Pattern
            .compile("^[a-zA-Zа-яА-Я]+$");//ALT+Enter -> check
    private final Pattern patternPhone = Pattern
            .compile("(\\d{10})");//ALT+Enter -> check
    private final Pattern patternCar = Pattern
            .compile("^[a-zA-Z0-9]+$");//ALT+Enter -> check
    private final Pattern pattern = Pattern
            .compile("(^[А-я]+)\\s+([А-я]+)\\s+(\\d{10})\\s+([А-я0-9\\d]+$)");//ALT+Enter -> check
    private final InlineKeyboardMarkupService inlineKeyboardMarkupService;
    private final ReplyKeyboardMarkupService replyKeyboardMarkupService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AdopterRepository adopterRepository;

    public UserRequestServiceImpl(InlineKeyboardMarkupService inlineKeyboardMarkupService,
                                  ReplyKeyboardMarkupService replyKeyboardMarkupService,
                                  TelegramBot telegramBot,
                                  UserService userService,
                                  UserRepository userRepository,
                                  AdopterRepository adopterRepository) {
        this.inlineKeyboardMarkupService = inlineKeyboardMarkupService;
        this.replyKeyboardMarkupService = replyKeyboardMarkupService;
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.userRepository = userRepository;
        this.adopterRepository = adopterRepository;
    }

    @Override
    public void sendMessageStart(Update update) {

        Message message = update.message();
        Long chatId = message.from().id();
        String text = message.text();
        String userName = update.message().from().firstName();
        long userId = update.message().from().id();

        if ("/start".equals(text)) {

            User user = userRepository.findByUserId(userId);

            if (user == null) {
                greetingNewUser(chatId, userName);
                userService.addUser(userId, userName, UserType.DEFAULT, UserStatus.APPROVE);

            } else if (user.getUserType() == UserType.DEFAULT && user.getUserStatus() == UserStatus.APPROVE) {
                greetingNotNewUser(chatId, userName);

            } else if (user.getUserType() == UserType.GUEST && user.getUserStatus() == UserStatus.APPROVE) {
                greetingGuest(chatId, userName);

            } else if (user.getUserType() == UserType.ADOPTER && user.getUserStatus() == UserStatus.APPROVE) {
                if (user.getShelterType() == ShelterType.CAT_SHELTER) {
                    greetingAdopterCatShelter(chatId, userName);
                } else if (user.getShelterType() == ShelterType.DOG_SHELTER) {
                    greetingAdopterDogShelter(chatId, userName);
                }

            } else if (user.getUserType() == UserType.VOLUNTEER && user.getUserStatus() == UserStatus.APPROVE) {
                greetingVolunteer(chatId, userName);

            } else {
                blockedUser(chatId, userName);
            }
        }
    }

    private void greetingVolunteer(long chatId, String name) {
        /*SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETING_VOLUNTEER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }*/
    }

    private void greetingGuest(long chatId, String name) {
        SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETING_GUEST, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingAdopterDogShelter(long chatId, String name) {

        SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETING_ADOPTER_DOG_SHELTER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelterReport());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingAdopterCatShelter(long chatId, String name) {

        SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETING_ADOPTER_CAT_SHELTER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterReport());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void blockedUser(long chatId, String name) {
        SendMessage sendMessage =
                new SendMessage(chatId, String.format(NOT_GREETING_USER, name));

        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingNotNewUser(Long chatId, String name) {

        SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETINGS_NOT_NEW_USER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingNewUser(Long chatId, String name) {

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
                case CLICK_VISIT_CAT:

                    updateUserInGuestCatShelter(update);

                    break;
                case CLICK_VISIT_DOG:

                    updateUserInGuestDogShelter(update);

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

        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_DOG_SHELTER);
        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelter());

        sendMessage(sendMessage);
    }

    private void getCatShelterClick(long chatId) {

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

    public void updateUserInGuestCatShelter(Update update) {

        Message message = update.callbackQuery().message();
        Long chatId = message.chat().id();
        String text = message.text();
        long userId = message.from().id();
        String userName = message.from().firstName();

        String firstName;
        String lastName;
        String phoneNumber;
        String carNumber;
        ShelterType shelterType = ShelterType.CAT_SHELTER;
        UserType userType = UserType.GUEST;
        UserStatus userStatus = UserStatus.APPROVE;

        if ("Выберете, пожалуйста, вариант из предложенного меню!".equals(text)) {

            sendMessage(chatId, """
                    Чтобы записаться на посещение,
                    нужно заполнить анкету:
                    Напишите Ваше Имя, Фамилию, номер телефона(без кода страны), номер машины в формате:
                    Иван Иванов 1234567890 а123аа""");


        } else if (text != null) {

            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {

                firstName = matcher.group(1);
                lastName = matcher.group(2);
                phoneNumber = matcher.group(3);
                carNumber = matcher.group(4);

                User guest = new User(userId,
                        userName,
                        firstName,
                        lastName,
                        phoneNumber,
                        carNumber,
                        shelterType,
                        userType,
                        userStatus);

                User user = userRepository.findByUserId(userId);
                userRepository.delete(user);
                userRepository.save(guest);

                sendMessage(chatId, "Анкета успешно заполнена!");
                getMainMenuClick(chatId);

            } else {
                sendMessage(chatId, "Некорректный формат!");
            }
        }
    }

    public void updateUserInGuestDogShelter(Update update) {

        Message message = update.callbackQuery().message();
        Long chatId = message.chat().id();
        String text = message.text();
        long userId = message.from().id();
        String userName = message.from().firstName();

        String firstName;
        String lastName;
        String phoneNumber;
        String carNumber;
        ShelterType shelterType = ShelterType.DOG_SHELTER;
        UserType userType = UserType.GUEST;
        UserStatus userStatus = UserStatus.APPROVE;

        sendMessage(chatId, """
                Чтобы записаться на посещение,
                нужно заполнить анкету:
                Напишите Ваше Имя, Фамилию, номер телефона(без кода страны), номер машины в формате:
                Иван Иванов 1234567890 а123аа""");

        if (text != null) {

            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {

                firstName = matcher.group(1);
                lastName = matcher.group(2);
                phoneNumber = matcher.group(3);
                carNumber = matcher.group(4);

                User guest = new User(userId,
                        userName,
                        firstName,
                        lastName,
                        phoneNumber,
                        carNumber,
                        shelterType,
                        userType,
                        userStatus);

                User user = userRepository.findByUserId(userId);
                userRepository.delete(user);
                userRepository.save(guest);

                sendMessage(chatId, "Анкета успешно заполнена!");
                getMainMenuClick(chatId);

            } else {
                sendMessage(chatId, "Некорректный формат!");
            }
        }
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

    /*public void updateUserInGuest(Update update, long chatId) {

        Message message = update.message();

        String text = message.text();

        String firstName = null;
        String lastName = null;
        String phoneNumber = null;
        String carNumber = null;

        sendMessage(chatId, "Чтобы записаться на посещение,\n" +
                "нужно заполнить анкету:\n" +
                "Напишите Ваше имя");

        if (text != null) {
            Matcher matcher = patternName.matcher(text);
            if (matcher.find()) {
                firstName = text;

                sendMessage(chatId,
                        "Напишите Вашу фамилию");

                if (text != null) {
                    Matcher matcher1 = patternName.matcher(text);
                    if (matcher1.find()) {
                        lastName = text;

                        sendMessage(chatId,
                                "Напишите Ваш номер телефона без кода страны в формате: XXXХХХХХХХ");

                        if (text != null) {
                            Matcher matcher2 = patternPhone.matcher(text);
                            if (matcher2.find()) {
                                phoneNumber = '7' + text;

                                sendMessage(chatId,
                                        "Напишите номер Вашей машины.");

                                if (text != null) {
                                    Matcher matcher3 = patternCar.matcher(text);
                                    if (matcher3.find()) {
                                        carNumber = text;
                                        long userId = update.message().from().id();

                                        userRepository.updateUserInGuestById(userId,
                                                firstName,
                                                lastName,
                                                phoneNumber,
                                                carNumber,
                                                UserType.GUEST,
                                                UserStatus.APPROVE);

                                        sendMessage(chatId, "Анкета успешно заполнена!");
                                        getMainMenuClick(chatId);
                                    } else {
                                        sendMessage(chatId, "Номер введен некорректно!");
                                    }
                                    //sendMessage(sendMessage3);
                                }

                            } else {
                                sendMessage(chatId, "Номер телефона введен некорректно!");
                            }
                            //sendMessage(sendMessage2);
                        } else {
                            sendMessage(chatId, "Некорректный формат фамилии!");
                        }
                    }
                    //sendMessage(sendMessage1);
                } else {
                    sendMessage(chatId, "Некорректный формат имени!");
                }
            }
            //sendMessage(sendMessage);
        }
    }*/
}
