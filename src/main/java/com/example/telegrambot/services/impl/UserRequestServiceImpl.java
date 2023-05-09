package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.exception.NotFoundReportException;
import com.example.telegrambot.listener.TelegramBotUpdatesListener;
import com.example.telegrambot.model.*;
import com.example.telegrambot.repository.*;
import com.example.telegrambot.services.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private final Pattern patternAdopter = Pattern
            .compile("(^\\d{10})\\s+(\\d)\\s+(\\d+$)");//ALT+Enter -> check
    private final Pattern pattern = Pattern
            .compile("(^[А-я]+)\\s+([А-я]+)\\s+(\\d{10})\\s+([А-я0-9\\d]+$)");//ALT+Enter -> check
    private final InlineKeyboardMarkupService inlineKeyboardMarkupService;
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
    final Map<Long, UserType> reportStateByChatId = new HashMap<>();
    final Map<Long, UserType> userCatStateByChatId = new HashMap<>();
    final Map<Long, UserType> userDogStateByChatId = new HashMap<>();
    private final Map<Long, UserType> messageStateByChatId = new HashMap<>();

    final Map<Long, UserType> adopterStateByChatId = new HashMap<>();
    private final Map<Long, String> stateByChatId = new HashMap<>();

    private final ShelterRepository shelterRepository;

    private static final int HOUR_OF_DAY = 18;
    private static final int MINUTE = 0;
    private static final int SECOND = 0;
    private static final long PERIOD_SECONDS = 24 * 60 * 60;

    public UserRequestServiceImpl(InlineKeyboardMarkupService inlineKeyboardMarkupService,
                                  TelegramBot telegramBot,
                                  UserService userService,
                                  UserRepository userRepository,
                                  ReportService reportService,
                                  ReportRepository reportRepository,
                                  DialogRepository dialogRepository,
                                  AdopterRepository adopterRepository,
                                  AnimalService animalService,
                                  AnimalRepository animalRepository,
                                  AdopterService adopterService,
                                  AdopterRepository adopterRepository1,
                                  ShelterRepository shelterRepository) {

        this.inlineKeyboardMarkupService = inlineKeyboardMarkupService;
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
    public boolean checkReport(Update update) {

        if (update.message() == null)
            return false;

        long chatId = update.message().from().id();

        if (reportStateByChatId.containsKey(chatId)) {
            handleAdopterReport(update);
            reportStateByChatId.remove(chatId);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkVolunteer(Update update) {

        if (update.message() == null)
            return false;

        long chatId = update.message().from().id();


        if (!stateByChatId.containsKey(chatId))
            return false;

        String state = stateByChatId.get(chatId);
        if (state == CLICK_CALL_A_VOLUNTEER) {
            handleCallVolunteer(update);
            stateByChatId.remove(chatId);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkAdopter(Update update) {

        if (update.message() == null)
            return false;

        long chatId = update.message().from().id();

        if (adopterStateByChatId.containsKey(chatId)) {
            recordingNewAnimals(update);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUserInGuestCat(Update update) {

        if (update.message() == null)
            return false;

        long chatId = update.message().from().id();

        if (userCatStateByChatId.containsKey(chatId)) {
            updateUserInGuestCatShelter(update);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUserInGuestDog(Update update) {

        if (update.message() == null)
            return false;

        long chatId = update.message().from().id();

        if (userDogStateByChatId.containsKey(chatId)) {
            updateUserInGuestDogShelter(update);
            return true;
        }
        return false;
    }

    private void handleCallVolunteer(Update update) {
        Message message = update.message();
        Long chatId = message.from().id();
        long userId = update.message().from().id();
        String text = message.text();
        String name = message.from().username();


        List<User> users = userService.getAllUsers();
        List<User> volunteers = new ArrayList<User>();
        for (User user : users) {
            if (user.getUserType() == UserType.VOLUNTEER)
                volunteers.add(user);
        }

        for (User volunteer : volunteers) {
            telegramBot.execute(new SendMessage(volunteer.getTelegramId(), "Усыновитель " +
                    "" + '@' + name + " послал сообщение: " + text));
        }

        SendMessage message1 = new SendMessage(chatId, "Первый освободившийся волонтёр ответит вам в ближайшее время");
        //message1.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterReport());
        telegramBot.execute(message1);
    }

    private void handleAdopterReport(Update update) {

        Message message = update.message();
        Long chatId = message.from().id();
        long userId = update.message().from().id();
        String text = message.text();

        User user = userRepository.findByTelegramId(userId);

        List<Dialog> dialogList = dialogRepository.findAll().stream().toList();
        for (Dialog dialog : dialogList) {
            dialogRepository.delete(dialog);
        }

        Report report = reportRepository.findReportByUserId(user);
        LocalDate dateReport = LocalDate.now();
        StatusReport statusReport = StatusReport.DEFAULT;

        if (update.message().photo() == null && text != null) {
            textReport = text;
            sendMessage(chatId, "текст заполнен!");
        } else if (update.message().photo() != null && text == null) {
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

        if (report == null) {
            reportService.saveReport(user,
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

        if (textReport != null && picture != null) {
            SendMessage message1 = new SendMessage(chatId, "Спасибо за полный отчёт," +
                    " результат проверки узнаете в течение дня!");
            message1.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterReport());
            telegramBot.execute(message1);

        } else if (textReport == null || picture == null) {
            SendMessage message1 = new SendMessage(chatId, "Спасибо за отчёт! К сожалению, он не полный, " +
                    "поэтому мы настоятельно рекомендуем прислать полный отчет, чтобы избежать последствий!");
            message1.replyMarkup(inlineKeyboardMarkupService.createButtonsCatShelterReport());
            telegramBot.execute(message1);
        }
    }

    @Override
    public void sendMessageStart(Update update) {

        Message message = update.message();
        Long chatId = message.from().id();
        String text = message.text();
        String firstName = update.message().from().firstName();
        String userName = update.message().from().username();
        long telegramId = update.message().from().id();

        if ("/start".equals(text)) {

            User user = userService.findUserByTelegramId(telegramId);

            if (user == null) {
                greetingNewUser(chatId, firstName);
                userService.addUser(telegramId, userName, UserType.DEFAULT, UserStatus.APPROVE);

            } else if (user.getUserType() == UserType.DEFAULT && user.getUserStatus() == UserStatus.APPROVE) {
                greetingNotNewUser(chatId, firstName);

            } else if (user.getUserType() == UserType.GUEST && user.getUserStatus() == UserStatus.APPROVE) {
                greetingGuest(chatId, firstName);

            } else if (user.getUserType() == UserType.ADOPTER && user.getUserStatus() == UserStatus.APPROVE) {

                Report report = reportRepository.findReportByUserId(user);
                if (report.getDateEndOfProbation() == LocalDate.now()) {
                    sendMessage(chatId, "Поздравляем, Вы прошли испытательный срок!");
                    userRepository.delete(user);
                }

                if (user.getShelterType() == ShelterType.CAT_SHELTER) {
                    List<Dialog> dialogList = dialogRepository.findAll().stream().toList();

                    for (Dialog dialog : dialogList) {
                        if (dialog.getGuestId().equals(user)) {
                            sendMessage(chatId, dialog.getTextMessage());
                        }
                    }
                    greetingAdopterCatShelter(chatId, firstName);

                } else if (user.getShelterType() == ShelterType.DOG_SHELTER) {
                    List<Dialog> dialogList = dialogRepository.findAll().stream().toList();

                    for (Dialog dialog : dialogList) {
                        if (dialog.getGuestId().equals(user)) {
                            sendMessage(chatId, dialog.getTextMessage());
                        }
                    }
                    greetingAdopterDogShelter(chatId, firstName);
                }

            } else if (user.getUserType() == UserType.VOLUNTEER && user.getUserStatus() == UserStatus.APPROVE) {
                greetingVolunteer(chatId, firstName);

            } else {
                blockedUser(chatId, firstName);
                List<Dialog> dialogList = dialogRepository.findAll().stream().toList();

                for (Dialog dialog : dialogList) {
                    if (dialog.getGuestId().equals(user)) {
                        sendMessage(chatId, dialog.getTextMessage());
                    }
                }
            }
        }
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

    public void greetingNewUser(Long chatId, String name) {

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
            sendingTimer(chatId);
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
                case CLICK_CALL_A_VOLUNTEER: {

                    stateByChatId.put(chatId, CLICK_CALL_A_VOLUNTEER);
                    SendMessage sendMessage = new SendMessage(chatId, "пожалуйста, введите сообщение для волонтера");
                    sendMessage(sendMessage);
                    break;
                }

                case CLICK_RECORDING_NEW_ANIMAL:

                    SendMessage sendMessage6 = new SendMessage(chatId, """
                            Чтобы записать усыновителя, нужно заполнить форму:
                            Напишите telegramId усыновителя, animalId, shelterId
                            в формате: 123456789 1 1""");
                    sendMessage(sendMessage6);

                    break;
                case CLICK_ARRANGEMENT_CAT_HOME:

                    getCatAtHomeClick(chatId);

                    break;
                case CLICK_ARRANGEMENT_DOG_HOME:

                    getDogAtHomeClick(chatId);

                    break;
                case CLICK_VISIT_CAT:

                    userCatStateByChatId.put(chatId, UserType.GUEST);
                    SendMessage sendMessage = new SendMessage(chatId, """
                            Чтобы записаться на посещение,
                            нужно заполнить анкету:
                            Напишите Ваше Имя, Фамилию, номер телефона(без кода страны), номер машины в формате:
                            Иван Иванов 1234567890 а123аа""");
                    sendMessage(sendMessage);

                    break;
                case CLICK_VISIT_DOG:

                    userDogStateByChatId.put(chatId, UserType.GUEST);
                    SendMessage sendMessage5 = new SendMessage(chatId, """
                            Чтобы записаться на посещение,
                            нужно заполнить анкету:
                            Напишите Ваше Имя, Фамилию, номер телефона(без кода страны), номер машины в формате:
                            Иван Иванов 1234567890 а123аа""");
                    sendMessage(sendMessage5);
                    break;

                case CLICK_CHECK_REPORT:

                    getCheckReport(update);

                    break;
                case CLICK_FREE_MESSAGE:

                    messageStateByChatId.put(chatId, UserType.VOLUNTEER);
                    getFreeMessage(update);

                    break;
                case CLICK_REPORT_CAT:
                case CLICK_REPORT_DOG:

                    reportStateByChatId.put(chatId, UserType.ADOPTER);
                    SendMessage sendMessage1 = new SendMessage(chatId, """
                            Отправьте отчет о питомце:
                            фото питомца;
                            рацион питомца;
                            общее самочувствие и привыкание к новому мету;
                            изменение в поведении.""");
                    sendMessage(sendMessage1);

                    break;
                case CLICK_OK:

                    checkReportStatusOk();

                    sendMessage(chatId, "отчет принят!");

                    break;
                case CLICK_NOT_OK:

                    checkReportStatusNotOk();

                    SendMessage sendMessage2 = new SendMessage(chatId, "отчет не принят!");

                    sendMessage2.replyMarkup(inlineKeyboardMarkupService.createButtonsCheckReportNotOk());

                    sendMessage(sendMessage2);

                    break;
                case CLICK_EXTEND:

                    SendMessage sendMessage3 = new SendMessage(chatId, "На сколько продлить испытательный срок?");

                    sendMessage3.replyMarkup(inlineKeyboardMarkupService.createButtonsCheckReportNotOkExtend());

                    sendMessage(sendMessage3);

                    break;
                case CLICK_WARNING_REPORT:

                    sendWarningMessage(update);
                    sendMessage(chatId, "Предупреждение отправлено!");

                    break;
                case CLICK_DELETE_ADOPTER:

                    sendWarningDeleteAdopter(update);
                    sendMessage(chatId, "Пользователь заблокирован!");

                    break;
                case CLICK_EXTEND_14_DAY:

                    sendExtend14Day();
                    sendMessage(chatId, "Испытательный срок продлен на 14 дней!");

                    break;
                case CLICK_EXTEND_30_DAY:

                    sendExtend30Day();
                    sendMessage(chatId, "Испытательный срок продлен на 30 дней!");

                    break;
                case CLICK_RULES_REPORT_CAT:
                case CLICK_RULES_REPORT_DOG:

                    sendMessage(chatId, "После того как новый усыновитель забрал животное из приюта, " +
                            "он обязан в течение месяца присылать информацию о том, как животное чувствует " +
                            "себя на новом месте. В ежедневный отчет входит следующая информация:\n" +
                            "    - Фото животного.\n" +
                            "    - Рацион животного.\n" +
                            "    - Общее самочувствие и привыкание к новому месту.\n" +
                            "    - Изменение в поведении: отказ от старых привычек, приобретение новых.\n" +
                            "Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет. " +
                            "Каждый день волонтеры осматривают все присланные отчеты после 21:00. ");

                    break;
                case CLICK_CAT_SHELTER_OVERVIEW:

                    sendMessage(chatId, "Адрес приюта для кошек в Астане: улица Кошачья, 123.\n" +
                            "Кошачий приют в Астане работает ежедневно с 10:00 до 19:00.\n" +
                            "Место нахождение приюта на карте: " +
                            "https://www.google.com/maps/search/?api=1&query=" +
                            "%D0%BA%D0%BE%D1%88%D0%B0%D1%87%D0%B8%D0%B9+%D0%BF%D1%80%D0%B8" +
                            "%D1%8E%D1%82+%D0%B0%D1%81%D1%82%D0%B0%D0%BD%D0%B0\n" +
                            "Кошачий приют в Астане: +7 (717) 111-CATS (2287)\n" +
                            "Телефон охраны: +7 (717) 111-CATS (2288)");

                    break;
                case CLICK_DOG_SHELTER_OVERVIEW:

                    sendMessage(chatId, "Адрес приюта для собак в Астане: улица Собачья, 321.\n" +
                            "Собачий приют в Астане работает ежедневно с 10:00 до 19:00.\n" +
                            "Место нахождение приюта на карте: " +
                            "https://www.google.com/maps/search/?api=1&query=" +
                            "%D0%BA%D0%BE%D1%88%D0%B0%D1%87%D0%B8%D0%B9+%D0%BF%D1%80%D0%B8" +
                            "%D1%8E%D1%82+%D0%B0%D1%81%D1%82%D0%B0%D0%BD%D0%B0\n" +
                            "Собачий приют в Астане: +7 (717) 111-CATS (2287)\n" +
                            "Телефон охраны: +7 (717) 111-CATS (2288)");

                    break;
                case CLICK_SAFETY_PRECAUTIONS_CAT:

                    sendMessage(chatId, "Техника безопасности для кошачьего приюта в Астане может включать " +
                            "в себя следующие меры:\n" +
                            "\n" +
                            "\n" +
                            "Установка камер видеонаблюдения во всех помещениях кошачьего приюта, чтобы следить за" +
                            " поведением животных и присутствием посетителей.\n" +
                            "\n" +
                            "Введение системы идентификации сотрудников приюта, которая позволит контролировать " +
                            "доступ в здание.\n" +
                            "\n" +
                            "Регулярная обучение персонала правилам безопасности на рабочем месте, пожарной" +
                            " безопасности и первой помощи.\n" +
                            "\n" +
                            "Установка автоматической пожарной сигнализации и противопожарных систем, таких как " +
                            "огнетушители и система полива.\n" +
                            "\n" +
                            "Разделение помещений на зоны для кошек с различными заболеваниями и без них, чтобы" +
                            " предотвратить распространение инфекций и болезней.\n" +
                            "\n" +
                            "Контроль качества пищи и воды, которые используются для кормления кошек.\n" +
                            "\n" +
                            "Регулярная дезинфекция и уборка в помещениях, используемых для кошек.\n" +
                            "\n" +
                            "Установка противокражных систем для снаряжения и оборудования, используемого в приюте.\n" +
                            "\n" +
                            "Установка ограждений и замков на входных дверях, чтобы предотвратить выход кошек без " +
                            "разрешения.\n" +
                            "\n" +
                            "Обязательное ношение сотрудниками специальной одежды и обуви, чтобы предотвратить " +
                            "распространение инфекций.");

                    break;
                case CLICK_SAFETY_PRECAUTIONS_DOG:

                    sendMessage(chatId, "Техника безопасности для собачьего приюта в Астане может включать" +
                            " в себя следующие элементы:\n" +
                            "\n" +
                            "\n" +
                            "Ограждение: Это может быть высокое заборное сооружение или специальный электрический " +
                            "забор, который обеспечивает безопасность собакам и предотвращает их выход за территорию приюта.\n" +
                            "\n" +
                            "Внутреннее пространство: Внутри приюта стенки для собак должны быть прочными и надежными, " +
                            "чтобы предотвратить возможность их побега. Также, внутреннее пространство должно регулярно" +
                            " очищаться и дезинфицироваться, чтобы предотвратить распространение инфекций и заболеваний.\n" +
                            "\n" +
                            "Обучение персонала: Работники приюта должны быть обучены правильному обращению и уходу за" +
                            " собаками, а также знать, как реагировать на возможные опасные ситуации.\n" +
                            "\n" +
                            "Контроль посетителей: В приюте может быть установлена система контроля посетителей, " +
                            "которая обеспечивает безопасность для людей и собак. Также, может быть введена политика" +
                            " посещений, которая ограничивает посещения конкретными группами или индивидуальными " +
                            "посетителями.\n" +
                            "\n" +
                            "Системы мониторинга: Может быть установлены камеры наблюдения и другие системы " +
                            "мониторинга, которые помогают улучшить безопасность в приюте.");

                    break;
                case CLICK_CAR_PASS_CAT:

                    sendMessage(chatId, "Чтобы оформить пропуск для автомобиля, свяжитесь с охраной " +
                            "кошачьего приюта:\n" +
                            "Телефон охраны: +7 (717) 111-CATS (2288)");

                    break;
                case CLICK_CAR_PASS_DOG:

                    sendMessage(chatId, "Чтобы оформить пропуск для автомобиля, свяжитесь с охраной " +
                            "собачьего приюта:\n" +
                            "Телефон охраны: +7 (717) 111-CATS (2288)");

                    break;

            }
        }
    }

    private void sendExtend30Day() {

        User user = checkReport.getUserId();

        LocalDate dateEndOfProbation = LocalDate.now().plusMonths(1);

        reportService.updateDateEndOfProbationById(user,
                dateEndOfProbation);

        Dialog dialog =
                new Dialog(user, null, "Испытательный срок продлен на 30 дней!", LocalDate.now());

        dialogRepository.save(dialog);
    }

    private void sendExtend14Day() {

        User user = checkReport.getUserId();

        LocalDate dateEndOfProbation = LocalDate.now().plusWeeks(2);

        reportService.updateDateEndOfProbationById(user,
                dateEndOfProbation);

        Dialog dialog =
                new Dialog(user, null, "Испытательный срок продлен на 14 дней!", LocalDate.now());

        dialogRepository.save(dialog);
    }

    private void sendWarningDeleteAdopter(Update update) {

        String textMessage = "Вы не прошли испытательный срок, " +
                "в ближайшее время с Вами свяжется волонтер для " +
                "дальнейшего плана действия!" +
                "Отныне, Вы персона нон града и доступ в наш приют заблокирован!";

        LocalDate date = LocalDate.now();

        User guest = checkReport.getUserId();
        UserStatus userStatus = UserStatus.BLOCKED;

        Dialog dialog = new Dialog(guest, null, textMessage, date);

        dialogRepository.save(dialog);

        userService.updateStatusUserById(guest.getTelegramId(), userStatus);
    }

    private void sendWarningMessage(Update update) {

        Message message = update.callbackQuery().message();
        String textMessage = "Дорогой усыновитель, мы заметили, " +
                "что ты заполняешь отчет не так подробно, как необходимо." +
                " Пожалуйста, подойди ответственнее к этому занятию. " +
                "В противном случае волонтеры приюта будут обязаны самолично " +
                "проверять условия содержания животного";
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

        reportService.updateStatusReportById(user,
                statusReport);

    }

    private void checkReportStatusNotOk() {

        User user = checkReport.getUserId();

        StatusReport statusReport = StatusReport.NOT_ACCEPTED;

        reportService.updateStatusReportById(user,
                statusReport);
    }

    public void getFreeMessage(Update update) {

        Message message = update.callbackQuery().message();
        long chatId = message.chat().id();

        User guest = checkReport.getUserId();

        sendMessage(chatId, "Напишите пользователю " + '@' + guest.getTelegramNick() + " " +
                "" + guest.getFirstName() + " в личном сообщении!");
    }

    private void getCheckReport(Update update) {

        Message message = update.callbackQuery().message();
        long chatId = message.chat().id();

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
        SendMessage sendMessage =
                new SendMessage(chatId, "Отчет от " + name +
                        ", был отправлен " + checkReport.getDateReport() + " :\n" +
                        "отчет: " + checkReport.getReportText() + "\n" +
                        "фото: " + checkReport.getPicture().toString());

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

        Message message = update.message();
        long chatId = message.chat().id();
        String text = message.text();
        long userId = message.from().id();
        String userName = message.from().username();

        String firstName;
        String lastName;
        String phoneNumber;
        String carNumber;
        ShelterType shelterType = ShelterType.CAT_SHELTER;
        UserType userType = UserType.GUEST;
        UserStatus userStatus = UserStatus.APPROVE;

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
                greetingGuest(chatId, firstName);

            } else {
                sendMessage(chatId, "Некорректный формат!");
            }
        }
    }

    public void updateUserInGuestDogShelter(Update update) {

        Message message = update.message();
        long chatId = message.chat().id();
        String text = message.text();
        long userId = message.from().id();
        String userName = message.from().username();

        String firstName;
        String lastName;
        String phoneNumber;
        String carNumber;
        ShelterType shelterType = ShelterType.DOG_SHELTER;
        UserType userType = UserType.GUEST;
        UserStatus userStatus = UserStatus.APPROVE;

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
                greetingGuest(chatId, firstName);

            } else {
                sendMessage(chatId, "Некорректный формат!");
            }
        }
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
        String userName = message.from().firstName();
        long chatId = message.chat().id();
        long telegramId;
        long animalID;
        long shelterID;

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
                    greetingVolunteer(chatId, userName);
                } else {
                    sendMessage(chatId, "усыновитель уже есть в БД");
                }
            } else {
                sendMessage(chatId, "некорректно введены данные");
            }
        }
    }

    public void sendingTimer(long chatId) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), HOUR_OF_DAY, MINUTE, SECOND);
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }
        long initialDelay = Duration.between(now, nextRun).getSeconds();
        long period = PERIOD_SECONDS;
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Report reportLast = reportRepository.getReferenceById(chatId);
                if (reportLast.getDateReport().isBefore(LocalDate.now().minusDays(2))) {
                    sendMessage(chatId, "Вы находитесь на испытательном сроке с животным, " +
                            "пожалуйста, предоставьте отчёт за последние 2 дня.");
                }
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }
}