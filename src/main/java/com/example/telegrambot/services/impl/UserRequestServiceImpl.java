package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.listener.TelegramBotUpdatesListener;
import com.example.telegrambot.model.Report;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.AdopterRepository;
import com.example.telegrambot.repository.ReportRepository;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.HashMap;
import java.util.Map;

import static com.example.telegrambot.constants.ConstantValue.*;

/**
 * Бизнес-логика по работе запросов от пользователей.
 */
@Component
public class UserRequestServiceImpl implements UserRequestService {
    private static String textReport;
    private static byte[] picture;
//    private final Pattern patternName = Pattern
//            .compile("^[a-zA-Zа-яА-Я]+$");//ALT+Enter -> check
//    private final Pattern patternPhone = Pattern
//            .compile("(\\d{10})");//ALT+Enter -> check
//    private final Pattern patternCar = Pattern
//            .compile("^[a-zA-Z0-9]+$");//ALT+Enter -> check
    private final Pattern pattern = Pattern
            .compile("(^[А-я]+)\\s+([А-я]+)\\s+(\\d{10})\\s+([А-я0-9\\d]+$)");//ALT+Enter -> check
    private final InlineKeyboardMarkupService inlineKeyboardMarkupService;
    private final ReplyKeyboardMarkupService replyKeyboardMarkupService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final AdopterRepository adopterRepository;

    private final Map<Long, ReportStatus> reportStateByChatId = new HashMap<>();

    public UserRequestServiceImpl(InlineKeyboardMarkupService inlineKeyboardMarkupService,
                                  ReplyKeyboardMarkupService replyKeyboardMarkupService,
                                  TelegramBot telegramBot,
                                  UserService userService,
                                  UserRepository userRepository,
                                  ReportService reportService,
                                  ReportRepository reportRepository, AdopterRepository adopterRepository) {

        this.inlineKeyboardMarkupService = inlineKeyboardMarkupService;
        this.replyKeyboardMarkupService = replyKeyboardMarkupService;
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.userRepository = userRepository;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.adopterRepository = adopterRepository;
    }

    @Override
    public boolean checkReport(Update update) {
        if (update.message() == null)
            return false;

        long chatId = update.message().from().id();

        if (reportStateByChatId.containsKey(chatId)) {
            HandleAdopterReport(update);
            return true;
        }
        return false;
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

    private void HandleAdopterReport(Update update) {
        Message message = update.message();
        Long chatId = message.from().id();
        long userId = update.message().from().id();
        String text = message.text();

        var reportState = reportStateByChatId.get(chatId);
        String response = "";

        if (update.message().photo() != null && text != null) {
            picture = savePhoto(update, chatId);
            textReport = text;
            //saveReport(update, chatId);

            reportState = new ReportStatus(true, true);
        }
        else if (update.message().photo() != null && text == null) {
            picture = savePhoto(update, chatId);

            reportState.photo = true;
            response = "спасибо за фото";
        }
        else if (update.message().photo() == null && text != null) {
            textReport = text;
            //saveReport(update, chatId);

            reportState.text = true;
            response = "спасибо за текст";
        }

        if (reportState.text && reportState.photo) {
            reportStateByChatId.remove(chatId);
            response = "спасибо за отчёт, результат проверки узнаете в течении дня";

            User user = userRepository.findByUserId(userId);
            reportService.saveReport(user, LocalDate.now(), StatusReport.NOT_ACCEPTED, textReport, picture);
        }
        else
        {
            if (!reportState.photo)
                response += ", нужно еще фото";
            if (!reportState.text)
                response += ", нужен еще текст";
            reportStateByChatId.put(chatId, reportState);
        }

        var message1 = new SendMessage(chatId, response);
        if (reportState.text && reportState.photo)
            message1.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterReport());
        telegramBot.execute(message1);
    }

    private void greetingVolunteer(long chatId, String name) {
        SendMessage sendMessage =
                new SendMessage(chatId, String.format(GREETING_VOLUNTEER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsVolunteerMenu());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
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

    private byte[] savePhoto(Update update, long chatId) {
        Message message = update.message();

        PhotoSize photoSize = message.photo()[message.photo().length - 1];
        GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));

        byte[] result;
        try {
            result = telegramBot.getFileContent(getFileResponse.file());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private void saveReport(Update update, long chatId) {
    }

    @Override
    public void createButtonClick(Update update) {

        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            long chatId = callbackQuery.message().chat().id();
            long userId = callbackQuery.message().from().id();
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

                    sendMessage(chatId, """
                            Чтобы записаться на посещение,
                            нужно заполнить анкету:
                            Напишите Ваше Имя, Фамилию, номер телефона(без кода страны), номер машины в формате:
                            Иван Иванов 1234567890 а123аа""");

                    updateUserInGuestCatShelter(update); //метод вызывается, но пишет, что Cannot invoke "com.pengrad.telegrambot.model.Message.chat()" because "message" is null
                    break;
                case CLICK_VISIT_DOG:

                    updateUserInGuestDogShelter(update); //метод вызывается, но пишет, что Cannot invoke "com.pengrad.telegrambot.model.Message.chat()" because "message" is null

                    break;
                case CLICK_CHECK_REPORT:

                    getCheckReport(update);

                    break;
                case CLICK_FREE_MESSAGE:

//                    getFreeMessage(update);

                    break;
                case CLICK_REPORT_CAT:
                case CLICK_REPORT_DOG:
                    reportStateByChatId.put(chatId, new ReportStatus(false, false));
                    SendMessage sendMessage = new SendMessage(chatId, """
                            Отправьте отчет о питомце:
                            фото питомца;
                            рацион питомца;
                            общее самочувствие и привыкание к новому мету;
                            изменение в поведении.""");
                    sendMessage(sendMessage);
                    break;
            }
        }
    }

    /*
    private void reportEnter(Update update) {

        Message message = update.message();
        long chatId = message.chat().id();
        String text = message.text();
        long userId = message.from().id();

        User user = userRepository.findByUserId(userId);
        Report report = reportRepository.findReportByUserId(user);
//        String textReport;
//        byte[] picture;
        LocalDate dateReport = LocalDate.now();
//        LocalDate dateEndOfProbation = null;

        StatusReport statusReport;

        if (text != null) {
            textReport = text;
            sendMessage(chatId, "текст заполнен!");
        } else if (message.photo() != null) {
            PhotoSize photoSize = message.photo()[message.photo().length - 1];
            GetFileResponse getFileResponse = telegramBot.execute(
                    new GetFile(photoSize.fileId()));

            if (getFileResponse.isOk()) {
                try {
                    picture = telegramBot.getFileContent(getFileResponse.file());
                    sendMessage(chatId, "фото отправлено!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (textReport != null && picture != null) {
            statusReport = StatusReport.ACCEPTED;

        } else {
            statusReport = StatusReport.NOT_ACCEPTED;
        }

        if (report == null) {
            reportService.saveReport(userId,
                    dateReport,
                    statusReport,
                    textReport,
                    picture);
        } else {
            reportService.updateReportByUserId(user,
                    dateReport,
                    statusReport,
                    textReport,
                    picture);
        }
    }
*/

    private void getCheckReport(Update update) {

//        Report report =
//        SendMessage sendMessage = new SendMessage(chatId, GREETINGS_AT_THE_SHELTER_INFO);
//        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCheckReport());
//
//        sendMessage(sendMessage);
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

        logger.info("Handles update: {}", update);

        Message message = update.message();
        long chatId = message.chat().id();
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

//        if ("Выберете, пожалуйста, вариант из предложенного меню!".equals(text)) {

//            sendMessage(chatId, """
//                    Чтобы записаться на посещение,
//                    нужно заполнить анкету:
//                    Напишите Ваше Имя, Фамилию, номер телефона(без кода страны), номер машины в формате:
//                    Иван Иванов 1234567890 а123аа""");


        if (text != null) {

            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {

                firstName = matcher.group(1);
                lastName = matcher.group(2);
                phoneNumber = matcher.group(3);
                carNumber = matcher.group(4);

                userService.addGuest(userId,
                        userName,
                        userType,
                        shelterType,
                        userStatus,
                        firstName,
                        lastName,
                        phoneNumber,
                        carNumber);

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

                userService.addGuest(userId,
                        userName,
                        userType,
                        shelterType,
                        userStatus,
                        firstName,
                        lastName,
                        phoneNumber,
                        carNumber);

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

    class ReportStatus {
        boolean photo;
        boolean text;

        public ReportStatus(boolean photo, boolean text) {
            this.photo = photo;
            this.text = text;
        }
    }
}
