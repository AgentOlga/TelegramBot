package com.example.telegrambot.repository;

import com.example.telegrambot.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository <Shelter, Long> {


}
