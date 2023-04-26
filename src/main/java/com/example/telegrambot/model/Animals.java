package com.example.telegrambot.model;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
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
public class Animals {

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

    public Animals(String nickName, PetType petType, Color color, Sex sex) {
        this.nickName = nickName;
        this.petType = petType;
        this.color = color;
        this.sex = sex;
    }
}