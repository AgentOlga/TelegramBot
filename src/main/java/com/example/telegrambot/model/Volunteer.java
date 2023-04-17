package com.example.telegrambot.model;

import jakarta.persistence.*;

import lombok.*;


import java.util.Objects;

/**
 * волонтеры
 */
@Entity
@Table(name = "volunteers")
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long chatId;
    private String name;

}


