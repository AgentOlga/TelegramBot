package com.example.telegrambot.model;

import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность пользователей
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "telegram_nick")
    private String telegramNick;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "user_status")
    private UserStatus userStatus;
}
