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
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
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
                case CLICK_GREETING_WITH_CAT:
                {
                    String text = "Преимущества обращения в приют за кошкой\n" +
                            "\n" +
                            "Взять кошку из приюта - прекрасное решение по ряду причин:\n" +
                            "\n" +
                            "1. Сотрудники приюта подберут вам питомца, подходящего вам по характеру и образу жизни.\n" +
                            "\n" +
                            "2. Вы возьмете уже привитого и стерилизованного/кастрированного друга, то есть не придется заниматься всем этим вам.\n" +
                            "\n" +
                            "3. При выборе взрослой кошки отпадает необходимость и в приучении к лотку или когтеточке.\n" +
                            "\n" +
                            "4. И самое главное: вы подарите дом, заботу и любовь питомцу, который долгое время был один!";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_CAT));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_GREETING_WITH_DOG: {
                    String text = "В приюте вам приглянулся четвероногий друг, и вы готовы взять его домой. \n" +
                            "Что нужно делать в этом случае?\n" +
                            "      Когда вы берете питомца из приюта, вы почти ничего не знаете о нем и его жизни, " +
                            "поэтому лучше расспросить работников приюта.\n" +
                            " Хотя может оказаться так, что они тоже не знают историю вашего нового друга. В приют попадают разные животные: и бездомные," +
                            " и те, чьим хозяевам пришлось отдать их из-за переезда.\n" +
                            "         Дом (или улица), где до вашего знакомства жила собака, " +
                            "формируют ее темперамент и отношения с людьми, поэтому перед тем, " +
                            "как принести пушистого малыша домой, очень важно узнать как можно больше о его прошлом. Ваш новый друг может " +
                            "быть застенчив или испуган из-за всей этой шумихи." +
                            " Вот несколько советов о том, как подготовить дом и членов семьи к такому событию," +
                            " как появление нового друга; они также помогут и самой собаке легко пережить переезд.\n" +
                            "        Перед тем, как привести нового друга домой\n" +
                            "     Прежде всего, нужно подготовить дом и купить " +
                            "необходимые для питомца вещи. " +
                            "Продумайте все: от того какие миски для корма и " +
                            "свежей воды купить для питомца до того какой забор установить на участке, " +
                            "если вы живете за городом и то, какие игрушки вам понадобятся. (Мягкие плюшевые? Резиновые жевательные? Столько вариантов!)\n" +
                            "        Вот что обязательно должно быть у вас к появлению собаки в доме (список можно дополнить): " +
                            "миски, ошейник с жетоном, поводок, все для груминга, совок, пакеты и мягкая, удобная лежанка.\n" +
                            "       Берете домой собаку? Поставьте детские ограждения на пути в комнаты, куда питомцу нельзя.\n" +
                            "        Что касается питания, выбирайте корм для питомца в соответствии с возрастом, " +
                            "состоянием здоровья и индивидуальными потребностями Не расстраивайтесь, если сначала питомцу не понравится корм." +
                            " Приучение собаки к новому режиму питания происходит путем проб и ошибок. На это может уйти несколько дней.\n" +
                            "          Вы берете собаку, и это ваш первый питомец?" +
                            "Найдите хорошего ветеринарного врача в вашем районе и сразу же отведите собаку к нему на осмотр. " +
                            "Если у вас уже есть ветеринарный врач, который консультирует вас по поводу ваших питомцев," +
                            " свяжитесь с ним до того, как привезете нового друга домой, и попросите дать советы о том, как обеспечить собаке теплый прием.";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_DOC_CAT: {
                    String text = "Основные условия:\n" +
                            "Мы отдаем животное строго по договору\n" +
                            "Мы обязательно отслеживаем судьбу наших \"выпускников\"\n" +
                            "Мы не отдаем животных на цепь, только в квартиру, в загородный дом или вольер\n" +
                            "Мы проверяем условия содержания у будущих владельцев\n" +
                            "Мы сами привозим наших животных будущим хозяевам\n" +
                            "Мы  всегда благодарны вам за фотоотчеты \"";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_CAT));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_DOC_DOG:
                {
                    String text = "Основные условия:\n" +
                            "Мы отдаем животное строго по договору\n" +
                            "Мы обязательно отслеживаем судьбу наших выпускников\"\n" +
                            "Мы не отдаем животных на цепь, только в квартиру, в загородный дом или вольер\n" +
                            "Мы проверяем условия содержания у будущих владельцев\n" +
                            "Мы сами привозим наших животных будущим хозяевам\n" +
                            "Мы  всегда благодарны вам за фотоотчеты \"";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_TRANSPORT_CAT:
                {
                    String text = "Волонтеры приюта могут привезти питомца к вам домой, " +
                            "или вы можете самостоятельно забрать питомца, " +
                            "также можно заказать зоотакси" +
                            " https://zoon.kz/astana/vet/type/zootaksi/";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_CAT));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_TRANSPORT_DOG:
                {
                    String text = "Приют может привести питомца или вы можете заказать зоотакси" +
                            "https://zoon.kz/astana/vet/type/zootaksi/";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_REASONS_FOR_REFUSAL_CAT:
                {
                    String text = "-  Большое количество животных дома \n" +
                            " -  Нестабильные отношения в семье \n" +
                            " -  Наличие маленьких детей \n" +
                            " -  Съемное жилье \n" +
                            " -  Животное в подарок или для работы                         \n" +
                            " Источник: https://kot-pes.com/5-prichin-pochemu-v-priyute-vam-mogut-ne-dat-zhivotnoe/";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_CAT));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_REASONS_FOR_REFUSAL_DOG:
                {
                    String text = " -  Большое количество животных дома \n" +
                            " -  Нестабильные отношения в семье \n" +
                            " -  Наличие маленьких детей \n" +
                            " -  Съемное жилье \n" +
                            " -  Животное в подарок или для работы                         \n" +
                            " Источник: https://kot-pes.com/5-prichin-pochemu-v-priyute-vam-mogut-ne-dat-zhivotnoe/";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_CYNOLOGIST_ADVICE:
                {
                    String text = "Приютская собака пуглива. " +
                            "Обращайтесь с ней ласково! Часто в приюте собаки теряют доверие к человеку и не ждут от него ничего хорошего, поэтому они могут забиваться в угол, зажмуривать глаза и вжимать голову в плечи при попытке нового хозяина ее погладить.  Собака может ложиться на землю и отказаться идти. И, возможно, даже случайно нагадить, в том числе в вашей машине, в которой вы повезете ее в новый дом! На полное восстановление доверия собаки понадобится несколько недель (ветеринарные врачи называют срок в полгода), и вы не имеете права на ошибку: в глазах своего питомца в всегда должны быть Самым Добрым и Самым Любящим Хозяином. Со временем стресс пройдет, и собака заново научится доверять людям. Не стоит в первый же день пытаться растормошить собаку, заставить ее играть или совершать долгие прогулки: животное станет бояться вас еще больше. " +
                            "Дайте ей время успокоиться и привыкнуть к новой обстановке – " +
                            "она ответит привязанностью на ваше терпение и будет обожать свой дом.";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_FIND_CYNOLOGIST:
                {
                    String text = "АСТАНА\n" +
                            "КИНОЛОГИЧЕСКИЙ ЦЕНТР К-9" +
                            "https://k-9.kz/nursultan_kcentre/lp/";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_HOW_TO_ADOPT_A_DOG));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_PUPPY:
                {
                    String text = "Когда вы заводите щенка, первым делом нужно найти хорошую ветеринарную клинику, расположенную в вашем районе. " +
                            "Зарегистрируйтесь в ней, если не сделали этого раньше. " +
                            "Так вы сможете быстро получить медицинскую помощь, если вашему питомцу станет плохо, понадобится сделать прививку или стерилизацию.\n" +
                            "Пока вы находитесь в ветеринарной клинике, узнайте о лучших условиях страхования домашних животных — это позволит вам покрыть любые неожиданные расходы, связанные с лечением питомца.\n" +
                            "Для полноценного развития вашего щеночка крайне важно, чтобы он постоянно общался как с другими собаками, так и с людьми. " +
                            "Ветеринарный врач обязательно расскажет о принципах обучения и дрессировки щенят, а практические занятия вы можете выполнять самостоятельно. " +
                            "Обучение — это отличное развлечение, которое помогает собаке и владельцу подружится." +
                            " В то же время необходимо помнить, что в ходе обучения должны использоваться только те модели дрессировки, которые основаны на похвале и поощрении. Никогда не наказывайте питомца и не используйте при обучении ошейник-удавку, а также ни в коем случае не применяйте иные насильственные методы дрессировки! Если вы берете животное из приюта, его сотрудники наверняка смогут порекомендовать вам профессионального тренера. Вы можете сами найти дрессировщика, который живет неподалеку от вашего дома.\n" +
                            "Всем, кто заводит щенка, рекомендуют прикрепить специальную идентификационную бирку, содержащую информацию о питомце, на его ошейник. По закону в общественных местах на нем всегда должен быть ошейник. Кроме того, собаку рекомендуется чипировать.";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_ARRANGEMENT_DOG_HOME));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_DOG_BIG:
                {
                    String text = "https://www.adaptil.com/ru/Blog/6-faktorov-kotorye-sleduet-uchest-esli-vy-hotite-vzyat-vzrosluyu-sobaku-iz-priyuta";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_ARRANGEMENT_DOG_HOME));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_DOG_INVALID:
                {
                    String text = "Подумайте, стоит ли брать собаку с особыми потребностями. Это собаки, которым нужно постоянное наблюдение у ветеринара, " +
                            "собаки-инвалиды, собаки, которые подвергались насилию и теперь у них есть поведенческие или эмоциональные проблемы.\n" +
                            "Постарайтесь понять потребности собаки, прежде чем выразите свое желание \"усыновить\" ее. " +
                            "Собаку с хроническим заболеванием придется часто возить к ветеринару. " +
                            "Убедитесь, что вы сможете оплачивать эти визиты и расходы на содержание и уход за своей собакой.\n" +
                            "Выделите специальное время для собаки. " +
                            "Многие собаки нервничают, когда оказываются в новом доме, особенно это касается собак с особыми потребностями." +
                            " Постарайтесь уделять собаке как можно больше времени, чтобы она привыкла к вам, к другим членам вашей семьи, к новому дому.\n" +
                            "Спросите в приюте или на передержке: \"" +
                            "Что особенного мне нужно будет делать, чтобы обеспечить должный уход этой собаке?\"\n";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_ARRANGEMENT_DOG_HOME));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_KITTY:
                {
                    String text = "Безопасный и функциональный дом с точки зрения человека может стать смертельно опасным для кошки. " +
                            "Особенно для шаловливых котят, которые везде и во все норовят сунуть свой носик, лизнуть и куснуть.\n" +
                            "\n" +
                            "Поэтому, заводя питомца, необходимо оценить все возможные риски и подготовить свой дом для нового обитателя. " +
                            "Итак, все, чем можно отравиться, прячем подальше. Бытовая химия,порошки, " +
                            "шампуни, дезинфицирующие средства, лекарства должны быть вне зоны досягаемости котенка." +
                            " Все, что может разбиться и ранить осколками, а также все, что сердцу дорого, " +
                            "тоже должна постигнуть та же участь. Уделите особое внимание вашим комнатным растениям, " +
                            "может статься, что некоторые из них содержат ядовитый сок в стеблях и листьях, это не страшно для человека, " +
                            "но опасно для кошки, которая, возможно, захочет поживать зелень.   " +
                            "Компьютерные, телевизионные и телефонные провода нужно тоже спрятать от острых зубов котят. " +
                            "Для этих целей существуют специальные пластиковые короба и трубки, " +
                            "которые пускаются либо вдоль плинтуса, либо поднимаются под потолок." +
                            "Прежде чем котенок или взрослая кошка, впервые попавшая в дом, " +
                            "начнет царапать мебель и стены, нужно приобрести когтеточку – " +
                            "высокую, чтобы взрослая особь могла встать на задние лапки и вытянуться во весь рост. " +
                            "Обычно кошки привыкают к своему тренажеру и не покушаются на другие уголки квартиры. " +
                            "Если все же был страстно облюбован угол дивана и никакие отпугиватели не помогают, " +
                            "то придется смириться и оббить этот угол плотным материалом  или приладить когтеточку к нему.\n" +
                            "\n" +
                            "Кроме того вы можете обернуть веревку вокруг стула и ножки стола, чтобы создать когтеточки в разных уголках квартиры.";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_ARRANGEMENT_CAT_HOME));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_CAT_BIG:
                {
                    String text = "одготовка к приезду животного\n" +
                            "\n" +
                            "Купите заранее миски для еды и воды. Главное, что стоит учитывать при их выборе – " +
                            "чтобы кошке было удобно ими пользоваться. Специалисты советуют приобрести мисочки трех видов: для влажного корма – неглубокую, для сухого корма – глубокую, а также устойчивую глубокую миску для воды. Пластмассовая посуда менее гигиеничная, чем стеклянная, металлическая или керамическая, поэтому стоит отдать предпочтение этим материалам.\n" +
                            "\n" +
                            "Что касается корма, стоит сказать, что резкая смена привычного питания – " +
                            "это тоже стресс для организма. Если вы берете котенка от заводчика, он подскажет вам, " +
                            "к какому корму приучено животное. Если вы решили взять кошку из приюта, вы также можете попробовать узнать, чем кормили кошку там. Если вас по каким-то причинам не устраивает уже привычная для вашего питомца еда и вы хотите перевести животное на корм другой марки, делайте это постепенно. Специалисты считают, что стоит кормить кошку как раньше неделю или две, а после добавить четверть желаемого корма от порции в привычный рацион с прибавкой 10% нового корма каждый день.\n" +
                            "Обязательно нужно заранее изучить рынок кормов для кошек, почитать отзывы и рекомендации, чтобы выбрать тот, который будет и полезен для вашего питомца, и по карману хозяину. Помните, что правильный корм очень важен для здоровья животного, и, сэкономив на стоимости и качестве корма, вы можете потом стать частым гостем ветеринара.\n" +
                            "\n" +
                            "Подготовьте лоток и определите, где он будет находиться. Кошки – очень чистоплотные животные, поэтому вашей задачей будет своевременно следить за состоянием лотка и держать его в чистоте. С точки зрения гигиены крайне не рекомендуется ставить лоток в комнате, кухне или коридоре. Животному нужно уединение во время пользования туалетом, поэтому эти места также не подходят с точки зрения самой кошки и ее потребностей. Однако если ваша жилплощадь не позволяет разместить лоток в более укромном и подходящем для этого месте (туалет, ванная), стоит обратить внимание на лоток-домик. Учитывайте габариты животного при выборе такого туалета, а также не забывайте следить за его чистотой, ведь такой лоток скрывает от ваших глаз все, что находится внутри и часто снабжен фильтром для ликвидации запаха, отчего, проходя мимо, можно не понять, что за кошкой нужно убрать.\n" +
                            "Помните, что для котенка нужен лоток не глубже 7 сантиметров, а взрослому животному требуется просторный и глубокий туалет. Не забудьте также купить совочек для уборки туалета.\n" +
                            "Что касается наполнителя, основные виды на рынке – минеральный, древесный и силикагелевый. Все они современны и безопасны. У всех, конечно, есть свои нюансы, плюсы и минусы, так что стоит заранее почитать о них в литературе или в Интернете, а также понаблюдать за реакцией животного на конкретный наполнитель.\n" +
                            "\n" +
                            "Подумайте о месте для сна Вашего питомца. В магазинах очень много лежанок и домиков для кошек, поэтому выбор за вами. Желательно обустроить спальное место для животного в спокойном и теплом месте. Большинство кошечек спит в нескольких местах в доме, но для начала достаточно подготовить одно.\n" +
                            "Дополнительно стоит также приобрести когтеточку, расческу для ухода за шерстью и игрушки для досуга.\n" +
                            "\n" +
                            "Очень важный момент – переноска для кошки. Переноски могут быть пластиковые и матерчатые. Учитывайте габариты животного, чтобы ему не было слишком тесно, но и «болтаться» в переноске кошка тоже не должна. Матерчатая сумка-переноска лучше подойдет для транспортировки в холодную погоду, однако в жару животному в ней может быть душно. Также существенный минус – промокаемость. Однако в некоторых случаях именно такая переноска удобнее и коту, и хозяину. Главное помнить, что у сумки должно быть твердое дно, прочная сетка и хорошие замки.\n" +
                            "Пластиковый контейнер больше подойдет для крупных кошек. Он отвечает всем вышеперечисленным требованиям, безопаснее и гигиеничнее, поэтому многие отдают предпочтение именно этому варианту.\n" +
                            "\n";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_ARRANGEMENT_CAT_HOME));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
                case CLICK_CAT_INVALID:
                {
                    String text = "Травмы или тяжёлые инфекционные заболевания могут привести к инвалидности питомца. " +
                            "Наиболее распространённым у кошек считается паралич задних конечностей вследствие травм спинного мозга." +
                            " В такой ситуации кошка может передвигаться только на передних лапах. " +
                            "Некоторые владельцы не могут смириться с такой бедой и решаются на эвтаназию, " +
                            "но некоторые, наоборот, всячески стараются поддержать любимца. " +
                            "Конечно, питомцу потребуется уделять больше внимания и заботы, " +
                            "но животное, в отличие от человека, " +
                            "не будет испытывать психологических проблем от осознания своей инвалидности." +
                            "https://www.belanta.vet/vet-blog/koshki-invalidy-kak-uhazhivat/";
                    InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
                    buttons.addRow(new InlineKeyboardButton(BUTTON_BACK).callbackData(CLICK_ARRANGEMENT_CAT_HOME));
                    sendMessage2(chatId, text, buttons);
                    break;
                }
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

    private void sendMessage1(SendMessage message) {
        SendResponse sendResponse = telegramBot.execute(message);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void sendMessage2(long chatId, String text, InlineKeyboardMarkup buttons) {
        SendMessage message = new SendMessage(chatId, text);
        message.replyMarkup(buttons);
        sendMessage1(message);
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