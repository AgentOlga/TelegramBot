package com.example.telegrambot.repository;

import com.example.telegrambot.model.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopterRepository extends JpaRepository<Adopter, Long> {
}
