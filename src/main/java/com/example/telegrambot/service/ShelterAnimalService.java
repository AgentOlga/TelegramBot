package com.example.telegrambot.service;

import com.example.telegrambot.repository.SheltersRepository;
import org.springframework.stereotype.Service;


/**
 * Класс отвечает за консультации с новым пользователем через методы приветствия,
 * предоставления полной информации о том, как предстоит подготовиться человеку ко встрече с новым членом семьи,
 * записать контактные данные пользователя, позвать волонтера.
 */
@Service
public class ShelterAnimalService {

    private final SheltersRepository sheltersRepository;

    public ShelterAnimalService(SheltersRepository sheltersRepository, SheltersRepository sheltersRepository1) {

        this.sheltersRepository = sheltersRepository1;
    }

    /**
     * Метод выдает информацию из БД по ключу, переданному через параметр
     * @param message ключ для доступа к данным в таблице
     * @return Информация по указанному ключу
     */
    public String getInfoByRequest(String message) {
        return sheltersRepository.findInfoByName(message).getDetails();
    }


}
