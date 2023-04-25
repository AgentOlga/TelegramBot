package com.example.telegrambot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.telegrambot.model.Pet;
public interface PetRepository extends JpaRepository<Pet,Long> {

}
