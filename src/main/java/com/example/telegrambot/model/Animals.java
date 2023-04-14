package com.example.telegrambot.model;

import com.example.telegrambot.constants.animalsConst.Color;
import com.example.telegrambot.constants.animalsConst.PetType;
import com.example.telegrambot.constants.animalsConst.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс животный, в котором передает Id, имя животного, тип животного
 * цвет и пол
 * также аннотации lombok, которые генерируют сеттеры геттеры конструктор
 * и методы hashCode, equals и ToString
 */

@Data
@AllArgsConstructor
public class Animals {

    private long id;
    private String nickName;
    private PetType petType;
    private Color color;
    private Sex sex;

}