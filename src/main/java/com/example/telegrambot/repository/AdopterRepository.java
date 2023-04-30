package com.example.telegrambot.repository;

import com.example.telegrambot.constants.*;
import com.example.telegrambot.model.Adopter;
import com.example.telegrambot.model.Animal;
import com.example.telegrambot.model.Shelter;
import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {


    @Modifying
    @Query("UPDATE Adopter a SET " +
            "a.user = :user_id, " +
            "a.animal = :animal_id," +
            "a.shelter = :shelter_id" +
            " WHERE a.id = :id")
    void updateAdopterById(
            @Param("id") Long id,
            @Param("user_id") User user,
            @Param("animal_id") Animal animal,
            @Param("shelter_id") Shelter shelter);
}
