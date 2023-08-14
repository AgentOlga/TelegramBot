package com.example.telegrambot.services.impl;

import com.example.telegrambot.model.*;
import com.example.telegrambot.services.ValidationService;
import org.springframework.stereotype.Service;

/**
 * Бизнес логика по работе с валидацией.
 */
@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validate(Object object) {

        if (object instanceof User) {
            return ((User) object).getTelegramId() != 0
                    && ((User) object).getUserType() != null
                    && ((User) object).getUserStatus() != null

                    || ((User) object).getTelegramId() != 0
                    && ((User) object).getFirstName() != null
                    && ((User) object).getLastName() != null
                    && ((User) object).getPhoneNumber() != null
                    && ((User) object).getUserType() != null
                    && ((User) object).getUserStatus() != null

                    ||((User) object).getTelegramId() != 0
                    && ((User) object).getFirstName() != null
                    && ((User) object).getLastName() != null
                    && ((User) object).getAddress() != null
                    && ((User) object).getEmail() != null
                    && ((User) object).getPhoneNumber() != null
                    && ((User) object).getUserType() != null
                    && ((User) object).getUserStatus() != null;

        } else if (object instanceof Animal) {
            return ((Animal) object).getColor() != null
                    && ((Animal) object).getNickName() != null
                    && ((Animal) object).getPetType() != null
                    && ((Animal) object).getSex() != null;

        } else if (object instanceof Adopter) {
            return ((Adopter) object).getShelter() != null
                    && ((Adopter) object).getAnimal() != null;

        } else if (object instanceof Shelter) {
            return ((Shelter) object).getAddressShelter() != null
                    && ((Shelter) object).getTimeWork() != null
                    && ((Shelter) object).getDrivingDirections() != null
                    && ((Shelter) object).getPhoneShelter() != null
                    && ((Shelter) object).getPhoneSecurity() != null
                    && ((Shelter) object).getShelterType() != null;

        } else {
            return false;
        }
    }
}
