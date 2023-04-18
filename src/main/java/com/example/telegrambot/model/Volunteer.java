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
    @Column(name = "id")
    private long id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "name")
    private String name;

}


