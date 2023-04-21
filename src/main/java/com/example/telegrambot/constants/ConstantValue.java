package com.example.telegrambot.constants;

/**
 * Константные значения.
 */
public class ConstantValue {

    /**
     * Стандартные ответы на запросы.
     */
    public static final String GREETINGS_AT_THE_PET_SHELTER = "Здравствуйте, %s!\n" +
            "Добро пожаловать в чат приюта домашних животных города Астаны!\n" +
            ConstantValue.GREETINGS_MAIN_MENU;
    public static final String GREETINGS_MAIN_MENU =
            "Выберете, пожалуйста, приют, из которого хотите забрать питомца!";
    public static final String GREETINGS_AT_THE_CAT_SHELTER = "Вы выбрали приют для кошек!\n" +
            "Выберете, пожалуйста, вариант из предложенного меню!";
    public static final String GREETINGS_AT_THE_DOG_SHELTER = "Вы выбрали приют для собак!\n" +
            "Выберете, пожалуйста, вариант из предложенного меню!";
    public static final String GREETINGS_AT_THE_SHELTER_INFO =
            "Выберете, пожалуйста, вариант из предложенного меню!";

    /**
     * Название общих кнопок.
     */
    public static final String BUTTON_SHELTER_INFO = "Информация о приюте";
    public static final String BUTTON_HOW_TO_ADOPT = "Как взять питомца из приюта";
    public static final String BUTTON_SEND_REPORT = "Прислать отчет о питомце";
    public static final String BUTTON_SHELTER_OVERVIEW = "Обзор приюта";
    public static final String BUTTON_CAR_PASS = "Оформить пропуск для автомобиля";
    public static final String BUTTON_SAFETY_PRECAUTIONS = "Техника безопасности";
    public static final String BUTTON_VISIT = "Записаться на посещение";
    public static final String BUTTON_GREETING_WITH_PET = "Правила знакомства с питомцем";
    public static final String BUTTON_DOC_PET = "Документы для забора питомца";
    public static final String BUTTON_TRANSPORT_PET = "Транспортировка питомца";
    public static final String BUTTON_ARRANGEMENT_PET_HOME = "Обустройство дома для питомца";
    public static final String BUTTON_REASONS_FOR_REFUSAL = "Причины отказа";
    public static final String BUTTON_RULES_REPORT_PET = "Правила предоставления отчета";
    public static final String BUTTON_REPORT_PET = "Отчитаться";
    public static final String BUTTON_CALL_VOLUNTEER = "Позвать волонтера";
    public static final String BUTTON_BACK = "Назад";
    public static final String BUTTON_PET_INVALID = "С ограниченными возможностями";
    //public static final String BUTTON_MAIN_MENU = "Главное меню";

    /**
     * Название кнопок для КОШАЧЬЕГО приюта.
     */
    public static final String BUTTON_CAT_SHELTER = "Приют для кошек";
    public static final String BUTTON_KITTY = "Котенок";
    public static final String BUTTON_CAT_BIG = "Взрослый кот";
    //public static final String BUTTON_CALL_A_VOLUNTEER_CAT = "Позвать волонтера";


    /**
     * Название кнопок для СОБАЧЬЕГО приюта.
     */
    public static final String BUTTON_DOG_SHELTER = "Приют для собак";
    //public static final String BUTTON_CALL_A_VOLUNTEER_DOG = "Позвать волонтера";
    public static final String BUTTON_CYNOLOGIST_ADVICE = "Советы кинолога";
    public static final String BUTTON_FIND_CYNOLOGIST = "Найти кинолога";
    public static final String BUTTON_PUPPY = "Щенок";
    public static final String BUTTON_DOG_BIG = "Взрослый пес";

    /**
     * Текст под общими кнопками - callbackQuery.data().
     */
    public static final String CLICK_BACK_TO_SHELTER_TYPE = "click_Back_To_Shelter_Type";
    public static final String CLICK_CALL_A_VOLUNTEER = "click_Call_A_Volunteer_Cat";

    /**
     * Текст под кнопками КОШАЧЬЕГО приюта- callbackQuery.data().
     */
    public static final String CLICK_CAT_SHELTER = "click_Shelter_Cat";
    public static final String CLICK_CAT_SHELTER_INFO = "click_Cat_Shelter_Info";
    public static final String CLICK_HOW_TO_ADOPT_A_CAT = "click_How_To_Adopt_A_Cat";
    public static final String CLICK_SEND_A_CAT_REPORT = "click_Send_A_Cat_Report";
//    public static final String CLICK_CALL_A_VOLUNTEER = "click_Call_A_Volunteer_Cat";
    public static final String CLICK_CAT_SHELTER_OVERVIEW = "click_Cat_Shelter_Overview";
    public static final String CLICK_CAR_PASS_CAT = "click_Car_Pass_Cat";
    public static final String CLICK_SAFETY_PRECAUTIONS_CAT = "click_Safety_Cat";
    public static final String CLICK_VISIT_CAT = "click_Visit_Cat";
    public static final String CLICK_GREETING_WITH_CAT = "CLICK_GREETING_WITH_CAT";
    public static final String CLICK_DOC_CAT = "CLICK_DOC_CAT";
    public static final String CLICK_TRANSPORT_CAT = "CLICK_TRANSPORT_CAT";
    public static final String CLICK_ARRANGEMENT_CAT_HOME = "CLICK_ARRANGEMENT_CAT_HOME";
    public static final String CLICK_REASONS_FOR_REFUSAL_CAT = "CLICK_REASONS_FOR_REFUSAL_CAT";
    public static final String CLICK_RULES_REPORT_CAT = "CLICK_RULES_REPORT_CAT";
    public static final String CLICK_REPORT_CAT = "CLICK_REPORT_CAT";
    public static final String CLICK_KITTY = "CLICK_KITTY";
    public static final String CLICK_CAT_BIG = "CLICK_CAT_BIG";
    public static final String CLICK_CAT_INVALID = "CLICK_CAT_INVALID";

    /**
     * Текст под кнопками СОБАЧЬЕГО приюта- callbackQuery.data().
     */
    public static final String CLICK_DOG_SHELTER = "click_Dog_Shelter";
    public static final String CLICK_DOG_SHELTER_INFO = "click_Dog_Shelter_Info";
    public static final String CLICK_HOW_TO_ADOPT_A_DOG = "click_How_To_Adopt_A_Dog";
    public static final String CLICK_SEND_A_DOG_REPORT = "click_Send_A_Dog_Report";
//    public static final String CLICK_CALL_A_VOLUNTEER_DOG = "click_Call_A_Volunteer_Dog";
    public static final String CLICK_DOG_SHELTER_OVERVIEW = "click_Dog_Shelter_Overview";
    public static final String CLICK_CAR_PASS_DOG = "click_Car_Pass_Dog";
    public static final String CLICK_SAFETY_PRECAUTIONS_DOG = "click_Safety_Dog";
    public static final String CLICK_VISIT_DOG = "click_Visit_Dog";
    public static final String CLICK_GREETING_WITH_DOG = "CLICK_GREETING_WITH_DOG";
    public static final String CLICK_DOC_DOG = "CLICK_DOC_DOG";
    public static final String CLICK_TRANSPORT_DOG = "CLICK_TRANSPORT_DOG";
    public static final String CLICK_ARRANGEMENT_DOG_HOME = "CLICK_ARRANGEMENT_DOG_HOME";
    public static final String CLICK_REASONS_FOR_REFUSAL_DOG = "CLICK_REASONS_FOR_REFUSAL_DOG";
    public static final String CLICK_CYNOLOGIST_ADVICE = "CLICK_CYNOLOGIST_ADVICE";
    public static final String CLICK_FIND_CYNOLOGIST = "CLICK_FIND_CYNOLOGIST";
    public static final String CLICK_RULES_REPORT_DOG = "CLICK_RULES_REPORT_DOG";
    public static final String CLICK_REPORT_DOG = "CLICK_REPORT_DOG";
    public static final String CLICK_PUPPY = "CLICK_PUPPY";
    public static final String CLICK_DOG_BIG = "CLICK_DOG_BIG";
    public static final String CLICK_DOG_INVALID = "CLICK_DOG_INVALID";

    /**
     * Значения в моделях.
     */
    public static final String DEFAULT_TELEGRAM_NICK = "Пользователь без ника";

}
