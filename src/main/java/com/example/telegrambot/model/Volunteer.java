package com.example.telegrambot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.Objects;

/**
 * волонтеры
 */
@Entity
@Table(name = "volunteers")
@Data
@AllArgsConstructor

public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long chatId;
    private String name;

    public Volunteer(long id, long chatId, String name) {
        this.id = id;
        this.chatId = chatId;
        this.name = name;
    }

    public Volunteer() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Volunteer volunteer = (Volunteer) o;

        if (id != volunteer.id) return false;
        if (chatId != volunteer.chatId) return false;
        return Objects.equals(name, volunteer.name);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (chatId ^ (chatId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}


