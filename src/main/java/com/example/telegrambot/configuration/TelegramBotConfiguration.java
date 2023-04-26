package com.example.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Описание сущности, с которой будет работать Telegram Bot
 */
@Configuration
public class TelegramBotConfiguration {

    private final String token;

    public TelegramBotConfiguration(@Value("${telegram.bot.token}") String token) {
        this.token = token;
    }

    @Bean
    public TelegramBot telegramBot() {

        TelegramBot telegramBot = new TelegramBot(token);
        return telegramBot;

    }
}
