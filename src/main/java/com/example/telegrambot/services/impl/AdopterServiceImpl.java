package com.example.telegrambot.services.impl;

import com.example.telegrambot.exception.NotFoundAdopterException;
import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.AdopterRepository;
import com.example.telegrambot.services.AdopterService;
import com.example.telegrambot.services.ValidationService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Бизнес-логика по работе с усыновителями.
 */
@Service
public class AdopterServiceImpl implements AdopterService {

    private final AdopterRepository adopterRepository;
    private final ValidationService validationService;

    public AdopterServiceImpl(AdopterRepository adopterRepository, ValidationService validationService) {
        this.adopterRepository = adopterRepository;
        this.validationService = validationService;
    }

    @Override
    public Adopter saveAdopter(Adopter adopter) {
        if (!validationService.validate(adopter)) {
            throw new ValidationException(adopter.toString());
        }
        return adopterRepository.save(adopter);
    }

    @Override
    public void updateAdopterById(Long id,
                                  User user,
                                  Animal animal,
                                  Shelter shelter) {

        Adopter adopter = adopterRepository.getReferenceById(id);
        if (adopter == null) {
            throw new NotFoundAdopterException("Усыновитель не найден!");
        }
        adopterRepository.updateAdopterById(id, user, animal, shelter);
    }

    @Override
    public void deleteAdopterById(Long id) {

        Adopter adopter = adopterRepository.getReferenceById(id);
        if (adopter == null) {
            throw new NotFoundAdopterException("Усыновитель не найден!");
        }
        adopterRepository.delete(adopter);
    }

    @Override
    public Collection<Adopter> getAllAdopter() {
        return adopterRepository.findAll();
    }

    @Override
    public Adopter foundAdopterById(long id) {
        Adopter adopter = adopterRepository.getReferenceById(id);
        if (adopter == null) {
            throw new NotFoundAdopterException(toString());
        }
        return adopter;
    }

}
