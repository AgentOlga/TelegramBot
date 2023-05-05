package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.exception.NotFoundReportException;
import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.listener.TelegramBotUpdatesListener;
import com.example.telegrambot.model.*;
import com.example.telegrambot.repository.*;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.telegrambot.constants.ConstantValue.*;

/**
 * Бизнес-логика по работе запросов от пользователей.
 */
@Component
public class UserRequestServiceImpl implements UserRequestService {

    private static Report checkReport;
    private static String textReport;
    private static byte[] picture;

    private final Pattern patternAdopter = Pattern.compile("(^\\d{9})\\s+(\\d{2})\\s+(\\d+$)");//ALT+Enter -> check

    private final Pattern pattern = Pattern.compile("(^[А-я]+)\\s+([А-я]+)\\s+(\\d{10})\\s+([А-я0-9\\d]+$)");//ALT+Enter -> check
    private final InlineKeyboardMarkupService inlineKeyboardMarkupService;
    private final ReplyKeyboardMarkupService replyKeyboardMarkupService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final DialogRepository dialogRepository;

    private final AnimalService animalService;

    private final AnimalRepository animalRepository;
    private final AdopterService adopterService;

    private final AdopterRepository adopterRepository;

    private final ShelterRepository shelterRepository;

    public UserRequestServiceImpl(InlineKeyboardMarkupService inlineKeyboardMarkupService, ReplyKeyboardMarkupService replyKeyboardMarkupService, TelegramBot telegramBot, UserService userService, UserRepository userRepository, ReportService reportService, ReportRepository reportRepository, DialogRepository dialogRepository, AdopterRepository adopterRepository, AnimalService animalService, AnimalRepository animalRepository, AdopterService adopterService, AdopterRepository adopterRepository1, ShelterRepository shelterRepository) {

        this.inlineKeyboardMarkupService = inlineKeyboardMarkupService;
        this.replyKeyboardMarkupService = replyKeyboardMarkupService;
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.userRepository = userRepository;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.dialogRepository = dialogRepository;
        this.animalService = animalService;
        this.animalRepository = animalRepository;
        this.adopterService = adopterService;
        this.adopterRepository = adopterRepository1;
        this.shelterRepository = shelterRepository;
    }

    @Override
    public void sendMessageStart(Update update) {

        Message message = update.message();
        Long chatId = message.from().id();
        String text = message.text();
        String userName = update.message().from().firstName();
        long userId = update.message().from().id();

        if ("/start".equals(text)) {

            User user = userService.findUserByTelegramId(userId);

            if (user == null) {
                greetingNewUser(chatId, userName);
                userService.addUser(userId, userName, UserType.DEFAULT, UserStatus.APPROVE);

            } else if (user.getUserType() == UserType.DEFAULT && user.getUserStatus() == UserStatus.APPROVE) {
                greetingNotNewUser(chatId, userName);

            } else if (user.getUserType() == UserType.GUEST && user.getUserStatus() == UserStatus.APPROVE) {
                greetingGuest(chatId, userName);

            } else if (user.getUserType() == UserType.ADOPTER && user.getUserStatus() == UserStatus.APPROVE) {
                if (user.getShelterType() == ShelterType.CAT_SHELTER) {
                    List<Dialog> dialogList = dialogRepository.findAll().stream().toList();

                    for (Dialog dialog : dialogList) {
                        if (dialog.getGuestId().equals(user)) {
                            sendMessage(chatId, dialog.getTextMessage());
                        }
                    }
                    greetingAdopterCatShelter(chatId, userName);
                } else if (user.getShelterType() == ShelterType.DOG_SHELTER) {
                    List<Dialog> dialogList = dialogRepository.findAll().stream().toList();

                    for (Dialog dialog : dialogList) {
                        if (dialog.getGuestId().equals(user)) {
                            sendMessage(chatId, dialog.getTextMessage());
                        }
                    }
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
        SendMessage sendMessage = new SendMessage(chatId, String.format(GREETING_VOLUNTEER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsVolunteerMenu());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingGuest(long chatId, String name) {
        SendMessage sendMessage = new SendMessage(chatId, String.format(GREETING_GUEST, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingAdopterDogShelter(long chatId, String name) {

        SendMessage sendMessage = new SendMessage(chatId, String.format(GREETING_ADOPTER_DOG_SHELTER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsDogShelterReport());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingAdopterCatShelter(long chatId, String name) {

        SendMessage sendMessage = new SendMessage(chatId, String.format(GREETING_ADOPTER_CAT_SHELTER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterReport());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void blockedUser(long chatId, String name) {
        SendMessage sendMessage = new SendMessage(chatId, String.format(NOT_GREETING_USER, name));

        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingNotNewUser(Long chatId, String name) {

        SendMessage sendMessage = new SendMessage(chatId, String.format(GREETINGS_NOT_NEW_USER, name));

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsShelterTypeSelect());
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void greetingNewUser(Long chatId, String name) {

        SendMessage sendMessage = new SendMessage(chatId, String.format(GREETINGS_AT_THE_PET_SHELTER, name));

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

                case CLICK_RECORDING_NEW_ANIMAL:
                    recordingNewAnimals(update);
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

                    getFreeMessage(update);

                    break;
                case CLICK_REPORT_CAT:
                case CLICK_REPORT_DOG:

                    sendMessage(chatId, """
                            Отправьте отчет о питомце:
                            фото питомца;
                            рацион питомца;
                            общее самочувствие и привыкание к новому мету;
                            изменение в поведении.""");
                    reportEnter(update); //метод вызывается, но пишет, что Cannot invoke "com.pengrad.telegrambot.model.Message.chat()" because "message" is null

                    break;
                case CLICK_OK:

                    checkReportStatusOk();

                    sendMessage(chatId, "отчет принят!");

                    break;
                case CLICK_NOT_OK:

//                    checkReportStatusNotOk();

                    SendMessage sendMessage = new SendMessage(chatId, "отчет не принят!");

                    sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCheckReportNotOk());

                    sendMessage(sendMessage);

                    break;
                case CLICK_EXTEND:

                    SendMessage sendMessage1 = new SendMessage(chatId, "На сколько продлить испытательный срок?");

                    sendMessage1.replyMarkup(inlineKeyboardMarkupService.createButtonsCheckReportNotOkExtend());

                    sendMessage(sendMessage1);

                    break;
                case CLICK_WARNING_REPORT:

                    sendWarningMessage(update);
                    sendMessage(chatId, "Предупреждение отправлено!");

                    break;
                case CLICK_EXTEND_14_DAY:

                    sendExtend14Day();
                    sendMessage(chatId, "Испытательный срок продлен на 14 дней!");

                    break;
                case CLICK_EXTEND_30_DAY:

                    sendExtend30Day();
                    sendMessage(chatId, "Испытательный срок продлен на 30 дней!");

                    break;

            }
        }
    }

    private void sendExtend30Day() {

        User user = checkReport.getUserId();

        LocalDate dateEndOfProbation = LocalDate.now().plusMonths(1);

        reportService.updateDateEndOfProbationById(user, dateEndOfProbation);
    }

    private void sendExtend14Day() {

        User user = checkReport.getUserId();

        LocalDate dateEndOfProbation = LocalDate.now().plusWeeks(2);

        reportService.updateDateEndOfProbationById(user, dateEndOfProbation);
    }

    private void sendWarningMessage(Update update) {

        Message message = update.callbackQuery().message();
        long chatId = message.chat().id();
        String textMessage = "Дорогой усыновитель, мы заметили, " + "что ты заполняешь отчет не так подробно, как необходимо." + " Пожалуйста, подойди ответственнее к этому занятию. " + "В противном случае волонтеры приюта будут обязаны самолично " + "проверять условия содержания животного";
        long userId = message.from().id();

        LocalDate date = LocalDate.now();

        User guest = checkReport.getUserId();
        User volunteer = userService.findUserByTelegramId(userId);

        Dialog dialog = new Dialog(guest, volunteer, textMessage, date);

        dialogRepository.save(dialog);
    }

    private void checkReportStatusOk() {
        User user = checkReport.getUserId();

        StatusReport statusReport = StatusReport.ACCEPTED;

        reportService.updateStatusReportById(user, statusReport);

    }

    private void checkReportStatusNotOk() {
        User user = checkReport.getUserId();

        StatusReport statusReport = StatusReport.NOT_ACCEPTED;

        reportService.updateStatusReportById(user, statusReport);
    }

    public void getFreeMessage(Update update) {

        /*Message message = update.callbackQuery().message();
        long chatId = message.chat().id();
        String text = message.text();
        String textMessage;
        long userId = message.from().id();

        if (text != null) {
            textMessage = text;
            LocalDate date = LocalDate.now();

            User guest = checkReport.getUserId();
            User volunteer = userService.findUserByUserId(userId);

            Dialog dialog = new Dialog(guest, volunteer, textMessage, date);

            dialogRepository.save(dialog);

            sendMessage(chatId, "Сообщение отправлено!");
        }*/
    }

    private void reportEnter(Update update) {

        Message message = update.message();
        long chatId = message.chat().id();
        String text = message.text();
        long userId = message.from().id();

        User user = userRepository.findByTelegramId(userId);
        Report report = reportRepository.findReportByUserId(user);
//        String textReport;
//        byte[] picture;
        LocalDate dateReport = LocalDate.now();
//        LocalDate dateEndOfProbation = null;

        StatusReport statusReport = StatusReport.DEFAULT;

        Dialog dialog = dialogRepository.findDialogByUserId(user);
        dialogRepository.delete(dialog);

        if (text != null) {
            textReport = text;
            sendMessage(chatId, "текст заполнен!");
        } else if (message.photo() != null) {
            PhotoSize photoSize = message.photo()[message.photo().length - 1];
            GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));

            if (getFileResponse.isOk()) {
                try {
                    picture = telegramBot.getFileContent(getFileResponse.file());
                    sendMessage(chatId, "фото отправлено!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (report == null) {
            reportService.saveReport(user, dateReport, statusReport, textReport, picture);
        } else {
            reportService.updateReportByUserId(user, dateReport, statusReport, textReport, picture);
        }
    }

    private void getCheckReport(Update update) {

        Message message = update.callbackQuery().message();
        long chatId = message.chat().id();
        String text = message.text();
        long userId = message.from().id();

        List<Report> reportList = reportService.getAllReport().stream().toList();


        for (Report report : reportList) {
            if (report.getStatusReport() == StatusReport.DEFAULT) {
                checkReport = report;
                break;
            }
        }
        if (checkReport == null) {
            sendMessage(chatId, "Отчетов нет!");
            throw new NotFoundReportException("Отчетов нет!");
        }

        String name = checkReport.getUserId().getFirstName();
        SendMessage sendMessage = new SendMessage(chatId, "Отчет от " + name + " , был отправлен " + checkReport.getDateReport() + " :\n" + checkReport.getReportText() + checkReport.getPicture().toString());

        sendMessage.replyMarkup(inlineKeyboardMarkupService.createButtonsCheckReport());

        sendMessage(sendMessage);
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

                userService.addGuest(userId, userName, userType, shelterType, userStatus, firstName, lastName, phoneNumber, carNumber);

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

                userService.addGuest(userId, userName, userType, shelterType, userStatus, firstName, lastName, phoneNumber, carNumber);

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

    private void recordingNewAnimals(Update update) {


        Message message = update.message();
        String text = message.text();
        long chatId = message.chat().id();
        long telegramId;
        long animalID;
        long shelterID;

        sendMessage(chatId, """
                Чтобы записать усыновителя,
                нужно заполнить форму:
                Напишите  chatId, userId, animal, shelter""");

        if (text != null) {
            Matcher matcher = patternAdopter.matcher(text);
            if (matcher.find()) {

                telegramId = Long.parseLong(matcher.group(1));
                animalID = Long.parseLong(matcher.group(2));
                shelterID = Long.parseLong(matcher.group(3));

                User userId = userService.findUserByTelegramId(telegramId);
                Animal animalId = animalRepository.getReferenceById(animalID);
                Shelter shelterId = shelterRepository.getReferenceById(shelterID);

                Adopter adopter = new Adopter(userId, animalId, shelterId);
                Adopter adopterOne = adopterRepository.findAdopterByUserId(userId);

                if (adopterOne == null) {
                    adopterRepository.save(adopter);
                    sendMessage(chatId, "усыновитель добавлен");
                } else {
                    sendMessage(chatId, "усыновитель уже есть в БД");
                    throw new ValidationException("");
                }
                getMainMenuClick(chatId);

            } else {
                sendMessage(chatId, "некорректно введены данные");
            }
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
}
