package com.example.telegrambot.services.impl;

import com.example.telegrambot.exception.NotFoundAdopterException;
import com.example.telegrambot.exception.ValidationException;
import com.example.telegrambot.model.Adopter;
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
    public Adopter updateAdopter(Adopter adopter) {
        if (adopterRepository.findById(adopter.getId()).orElse(null) == null) {
            return null;
        }
        return adopterRepository.save(adopter);
    }

    @Override
    public Adopter deleteAdopter(Adopter adopter) {
        if (adopterRepository.findById(adopter.getId()).orElse(null) == null) {
            return null;
        }
        adopterRepository.delete(adopter);
        return adopter;
    }

    @Override
    public Collection<Adopter> getAllAdopter() {
        return adopterRepository.findAll();
    }

    @Override
    public Adopter foundAdopterById(long id) {
        Adopter adopter = adopterRepository.findById(id).orElse(null);
        if (adopter == null) {
            throw new NotFoundAdopterException(toString());
        }
        return adopter;
    }

}
