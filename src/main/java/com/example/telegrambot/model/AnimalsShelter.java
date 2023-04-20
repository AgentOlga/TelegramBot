package com.example.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * животные которые поступают в бота и количество животных
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalsShelter {
    private Animals animals;
    int quantity;
}