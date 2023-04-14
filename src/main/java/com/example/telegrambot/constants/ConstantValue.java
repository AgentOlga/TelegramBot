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
            "Выберете, пожалуйста, приют, из которого хотите забрать питомца!";
    public static final String GREETINGS_AT_THE_CAT_SHELTER = "Вы выбрали приют для кошек!\n" +
            "Выберете, пожалуйста, вариант из предложенного меню!";
    public static final String GREETINGS_AT_THE_DOG_SHELTER = "Вы выбрали приют для собак!\n" +
            "Выберете, пожалуйста, вариант из предложенного меню!";

    /**
     * Название кнопок для КОШАЧЬЕГО приюта.
     */
    public static final String BUTTON_CAT_SHELTER = "Приют для кошек";
    public static final String BUTTON_CAT_SHELTER_INFO = "Информация о приюте";
    public static final String BUTTON_HOW_TO_ADOPT_A_CAT = "Как взять питомца из приюта";
    public static final String BUTTON_SEND_A_CAT_REPORT = "Прислать отчет о питомце";
    public static final String BUTTON_CALL_A_VOLUNTEER_CAT = "Позвать волонтера";

    /**
     * Название кнопок для СОБАЧЬЕГО приюта.
     */
    public static final String BUTTON_DOG_SHELTER = "Приют для собак";
    public static final String BUTTON_DOG_SHELTER_INFO = "Информация о приюте";
    public static final String BUTTON_HOW_TO_ADOPT_A_DOG = "Как взять питомца из приюта";
    public static final String BUTTON_SEND_A_DOG_REPORT = "Прислать отчет о питомце";
    public static final String BUTTON_CALL_A_VOLUNTEER_DOG = "Позвать волонтера";

    /**
     * Текст под кнопками КОШАЧЬЕГО приюта- callbackQuery.data().
     */
    public static final String CLICK_CAT_SHELTER = "click_Shelter_Cat";
    public static final String CLICK_CAT_SHELTER_INFO = "click_Cat_Shelter_Info";
    public static final String CLICK_HOW_TO_ADOPT_A_CAT = "click_How_To_Adopt_A_Cat";
    public static final String CLICK_SEND_A_CAT_REPORT = "click_Send_A_Cat_Report";
    public static final String CLICK_CALL_A_VOLUNTEER_CAT = "click_Call_A_Volunteer_Cat";

    /**
     * Текст под кнопками СОБАЧЬЕГО приюта- callbackQuery.data().
     */
    public static final String CLICK_DOG_SHELTER = "click_Dog_Shelter";
    public static final String CLICK_DOG_SHELTER_INFO = "click_Dog_Shelter_Info";
    public static final String CLICK_HOW_TO_ADOPT_A_DOG = "click_How_To_Adopt_A_Dog";
    public static final String CLICK_SEND_A_DOG_REPORT = "click_Send_A_Dog_Report";
    public static final String CLICK_CALL_A_VOLUNTEER_DOG = "click_Call_A_Volunteer_Dog";
}
