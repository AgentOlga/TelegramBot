package com.example.telegrambot.repository;


import com.example.telegrambot.constants.ShelterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SheltersRepository extends JpaRepository<ShelterType,Long> {

    ShelterType findInfoByName(String name);
}