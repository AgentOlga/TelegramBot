package com.example.telegrambot.model;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.telegrambot.constants.ConstantValue.*;

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

    @Column(name = "shelter_type")
    private ShelterType shelterType;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "user_status")
    private UserStatus userStatus;

    //Конструктор для пользователя бота
    public User(long userId,
                String telegramNick,
                UserType userType,
                UserStatus userStatus) {
        this.userId = userId;
        setTelegramNick(telegramNick);
        this.userType = userType;
        this.userStatus = userStatus;
    }

    //Конструктор для гостя без машины
    public User(long userId,
                String telegramNick,
                String firstName,
                String lastName,
                String phoneNumber,
                ShelterType shelterType,
                UserType userType,
                UserStatus userStatus) {
        this.userId = userId;
        setTelegramNick(telegramNick);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        this.shelterType = shelterType;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    //Конструктор для гостя с машины

    public User(long userId,
                String telegramNick,
                String firstName,
                String lastName,
                String phoneNumber,
                String carNumber,
                ShelterType shelterType,
                UserType userType,
                UserStatus userStatus) {
        this.userId = userId;
        setTelegramNick(telegramNick);
        setFirstName(firstName);
        setLastName(lastName);
        setCarNumber(carNumber);
        setPhoneNumber(phoneNumber);
        this.shelterType = shelterType;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    //Конструктор для усыновителя

    public User(long userId,
                String telegramNick,
                String firstName,
                String lastName,
                String phoneNumber,
                String carNumber,
                String email,
                String address,
                ShelterType shelterType,
                UserType userType,
                UserStatus userStatus) {
        this.userId = userId;
        setTelegramNick(telegramNick);
        setFirstName(firstName);
        setLastName(lastName);
        setCarNumber(carNumber);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        this.address = address;
        this.shelterType = shelterType;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public void setTelegramNick(String telegramNick) {
        if (telegramNick == null || telegramNick.isEmpty() || telegramNick.isBlank()) {
            this.telegramNick = DEFAULT_TELEGRAM_NICK;
        } else {
            this.telegramNick = telegramNick;
        }
    }

    public void setFirstName(String firstName) {

        if (firstName.matches("^[a-zA-Zа-яА-Я]+$")
                && Character.isUpperCase(firstName.charAt(0))) {
            this.firstName = firstName;
        } else {
            throw new RuntimeException("Имя введено некорректно");
        }
    }

    public void setLastName(String lastName) {
        if (firstName.matches("^[a-zA-Zа-яА-Я]+$")
                && Character.isUpperCase(firstName.charAt(0))) {
            this.lastName = lastName;
        } else {
            throw new RuntimeException("Фамилия введена некорректно");
        }
    }

    public void setPhoneNumber(String phoneNumber) {

        phoneNumber = phoneNumber.replace("-", "");
        phoneNumber = phoneNumber.replace(" ", "");
        phoneNumber = phoneNumber.replace("+", "");

        if (phoneNumber.length() == 10) {
            this.phoneNumber = '7' + phoneNumber;
        } else if (phoneNumber.length() > 11) {
            throw new RuntimeException("Телефон слишком длинный");
        } else if (phoneNumber.length() < 10) {
            throw new RuntimeException("Телефон слишком короткий");
        } else if (phoneNumber.length() == 11 && phoneNumber.charAt(0) != '7') {
            throw new RuntimeException("Номер телефона не соответствует коду страны");
        }
    }

    public void setCarNumber(String carNumber) {
        if (carNumber == null || carNumber.isEmpty() || carNumber.isBlank()) {
            this.carNumber = "Без автомобиля";
        } else {
            this.carNumber = carNumber;
        }
    }

    public void setEmail(String email) {

        String correct = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(correct);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            this.email = email;
        } else {
            throw new RuntimeException("Эл.почта введена некорректно");
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
