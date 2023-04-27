package com.example.telegrambot.repository;

import com.example.telegrambot.constants.*;
import com.example.telegrambot.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    /**
     * Изменяем животное по идентификатору
     *
     * @param id       идентификатор
     * @param nickName кличка
     * @param petType  тип животного
     * @param color    цвет
     * @param sex      пол
     */
    @Modifying
    @Query("UPDATE Animal a SET " +
            "a.nickName = :nick_name, " +
            "a.petType = :pet_type," +
            "a.color = :color," +
            "a.sex = :sex" +
            " WHERE a.id = :id")
    void updateAnimalById(
            @Param("id") Long id,
            @Param("nick_name") String nickName,
            @Param("pet_type") PetType petType,
            @Param("color") Color color,
            @Param("sex") Sex sex);
}
