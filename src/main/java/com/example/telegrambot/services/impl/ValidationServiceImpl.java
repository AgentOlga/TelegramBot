package com.example.telegrambot.services.impl;

import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animals;
import com.example.telegrambot.model.User;
import com.example.telegrambot.model.Volunteer;
import com.example.telegrambot.services.ValidationService;
import org.springframework.boot.jdbc.metadata.AbstractDataSourcePoolMetadata;
import org.springframework.stereotype.Service;

/**
 * Бизнес логика по работе с валидацией.
 */
@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validate(Object object) {

        if (object instanceof User) {
            return ((User) object).getNickName() != null;

        } else if (object instanceof Volunteer) {
            return ((Volunteer) object).getName() != null
                    && ((Volunteer) object).getChatId() != 0;

        } else if (object instanceof Animals) {
            return ((Animals) object).getColor() != null
                    && ((Animals) object).getNickName() != null
                    && ((Animals) object).getPetType() != null
                    && ((Animals) object).getSex() != null;

        } else if (object instanceof Adopter) {
            return ((Adopter) object).getFirstName() != null
                    && ((Adopter) object).getLastName() != null
                    && ((Adopter) object).getAddress() != null
                    && ((Adopter) object).getEmail() != null
                    && ((Adopter) object).getPetType() != null
                    && ((Adopter) object).getPhoneNumber() != null
                    && ((Adopter) object).getStatus() != null;
        } else {
            return false;
        }
    }
}
