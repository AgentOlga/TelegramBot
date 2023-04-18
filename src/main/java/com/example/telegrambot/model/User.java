package com.example.telegrambot.model;

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
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "nick_name")
    private String nickName;

    public User(long userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
    }
}
