package com.example.telegrambot.services.impl;

import com.example.telegrambot.model.*;
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
            return ((User) object).getUserId() != 0
                    && ((User) object).getUserType() != null
                    && ((User) object).getUserStatus() != null

                    || ((User) object).getUserId() != 0
                    && ((User) object).getFirstName() != null
                    && ((User) object).getLastName() != null
                    && ((User) object).getPhoneNumber() != null
                    && ((User) object).getUserType() != null
                    && ((User) object).getUserStatus() != null

                    ||((User) object).getUserId() != 0
                    && ((User) object).getFirstName() != null
                    && ((User) object).getLastName() != null
                    && ((User) object).getAddress() != null
                    && ((User) object).getEmail() != null
                    && ((User) object).getPhoneNumber() != null
                    && ((User) object).getUserType() != null
                    && ((User) object).getUserStatus() != null;

        } else if (object instanceof Animals) {
            return ((Animals) object).getColor() != null
                    && ((Animals) object).getNickName() != null
                    && ((Animals) object).getPetType() != null
                    && ((Animals) object).getSex() != null;

        } else if (object instanceof Adopter) {
            return ((Adopter) object).getShelter() != null
                    && ((Adopter) object).getAnimals() != null;

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
