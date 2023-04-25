package com.example.telegrambot.model;


import jakarta.persistence.*;

import java.util.Objects;
import com.example.telegrambot.constants.TypeAnimal;

/**
 * Класс животных, в котором передается Id, имя животного, тип животного, порода животного,возраст животного
 * также аннотации lombok, которые генерируют сеттеры геттеры конструктор
 * и методы hashCode, equals и ToString
 */
@Entity(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    @Enumerated(value = EnumType.STRING)
    private TypeAnimal type;
    private String breed;

    private boolean healthRestrictions;

    public Pet(String name, int age, TypeAnimal type, String breed, boolean healthRestrictions) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.breed = breed;
        this.healthRestrictions = healthRestrictions;
    }

    public Pet() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public TypeAnimal getType() {
        return type;
    }

    public void setType(TypeAnimal type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public boolean isHealth_restrictions() {
        return healthRestrictions;
    }

    public void setHealth_restrictions(boolean healthRestrictions) {
        this.healthRestrictions = healthRestrictions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;

        if (age != pet.age) return false;
        if (!Objects.equals(id, pet.id)) return false;
        return Objects.equals(name, pet.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
}

