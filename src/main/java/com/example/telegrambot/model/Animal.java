package com.example.telegrambot.model;

import com.example.telegrambot.constants.Color;
import com.example.telegrambot.constants.PetType;
import com.example.telegrambot.constants.Sex;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс животный, в котором передает Id, имя животного, тип животного
 * цвет и пол
 * также аннотации lombok, которые генерируют сеттеры геттеры конструктор
 * и методы hashCode, equals и ToString
 */

@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "pet_type")
    private PetType petType;

    @Column(name = "color")
    private Color color;

    @Column(name = "sex")
    private Sex sex;

    public Animal(String nickName, PetType petType, Color color, Sex sex) {
        setNickName(nickName);
        setPetType(petType);
        setColor(color);
        setSex(sex);
    }

    public void setNickName(String nickName) {
        if (nickName == null || nickName.isEmpty() || nickName.isBlank()) {
            throw new RuntimeException("Имя животного введено некорректно!");
        } else {
            this.nickName = nickName;
        }
    }

    public void setPetType(PetType petType) {
        if (petType == null) {
            throw new RuntimeException("Тип животного введен некорректно!");
        } else {
            this.petType = petType;
        }
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new RuntimeException("Цвет животного введен некорректно!");
        } else {
            this.color = color;
        }
    }

    public void setSex(Sex sex) {
        if (sex == null) {
            throw new RuntimeException("Пол животного введен некорректно!");
        } else {
            this.sex = sex;
        }
    }
}