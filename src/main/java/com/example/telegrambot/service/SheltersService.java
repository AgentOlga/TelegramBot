package com.example.telegrambot.service;

import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.repository.SheltersRepository;
import org.springframework.stereotype.Service;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.repository.SheltersRepository;

import java.util.Collection;


/**
 * Сервис который осдержит методы по добавлению питомцев, волонтеров и необходимой информации для

 * @see SheltersService
 */
@Service
public class SheltersService {

    private final SheltersRepository sheltersRepository;

    public SheltersService(SheltersRepository sheltersRepository) {
        this.sheltersRepository = sheltersRepository;
    }

    /**
     * Метод обновляет необходимую информацию в базу данных.
     *
     * @return Shelter
     */
    public Shelter editInfo(Shelter shelter){
        if(sheltersRepository.findInfoByName(shelter.getName()) == null){

        }
        return shelter;
    }


    public Object getAllInfo() {
        return null;
    }
}
