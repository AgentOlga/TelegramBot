package com.example.telegrambot;
import lombok.Getter;

import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public final class Bot  extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "HomelessPetsHouseBot";
    }

    @Override
    public String getBotToken() {
        return "5793615759:AAG83enbJ20n6VHY17F7UFrQESYieArPkaY";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
    }
}
