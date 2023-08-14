package com.example.telegrambot.repository;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Выводим пользователя по идентификатору в телеграмм
     *
     * @param telegramId идентификатор в телеграмм
     * @return найденный пользователь
     */
    @Query("SELECT u FROM User u WHERE u.telegramId = :telegram_id")
    User findByTelegramId(@Param("telegram_id") Long telegramId);

    @Query("SELECT u FROM User u")
    List<User> getAllUsers();

    /**
     * Изменяем пользователя до гостя
     *
     * @param telegramId      идентификатор в телеграмм
     * @param firstName   имя гостя
     * @param lastName    фамилия гостя
     * @param phoneNumber телефон гостя
     * @param carNumber   номер машины гостя
     * @param userType    тип пользователя
     * @param userStatus  статус пользователя
     */
    @Modifying
    @Query("UPDATE User u SET " +
            "u.firstName = :first_name, " +
            "u.lastName = :last_name," +
            "u.phoneNumber = :phone_number," +
            "u.carNumber = :car_number," +
            "u.shelterType = :shelter_type," +
            "u.userType = :user_type," +
            "u.userStatus = :user_status" +
            " WHERE u.telegramId = :telegram_id")
    void updateUserInGuestById(
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("phone_number") String phoneNumber,
            @Param("car_number") String carNumber,
            @Param("shelter_type") ShelterType shelterType,
            @Param("user_type") UserType userType,
            @Param("user_status") UserStatus userStatus,
            @Param("telegram_id") Long telegramId);

    /**
     * Изменяем гостя до усыновителя/волонтера
     *
     * @param telegramId      идентификатор в телеграмм
     * @param firstName   имя усыновителя/волонтера
     * @param lastName    фамилия усыновителя/волонтера
     * @param phoneNumber номер телефона усыновителя/волонтера
     * @param carNumber   номер машины усыновителя/волонтера
     * @param userType    тип пользователя
     * @param userStatus  статус пользователя
     * @param email       эл.почта усыновителя/волонтера
     * @param address     адрес усыновителя/волонтера
     */
    @Modifying
    @Query("UPDATE User u SET u.firstName = :first_name, " +
            "u.lastName = :last_name," +
            "u.phoneNumber = :phone_number," +
            "u.carNumber = :car_number," +
            "u.shelterType = :shelter_type," +
            "u.userType = :user_type," +
            "u.userStatus = :user_status," +
            "u.email = :email," +
            "u.address = :address" +
            " WHERE u.telegramId = :telegram_id")
    void updateGuestInAdopterById(@Param("telegram_id") long telegramId,
                                  @Param("first_name") String firstName,
                                  @Param("last_name") String lastName,
                                  @Param("phone_number") String phoneNumber,
                                  @Param("car_number") String carNumber,
                                  @Param("shelter_type") ShelterType shelterType,
                                  @Param("user_type") UserType userType,
                                  @Param("user_status") UserStatus userStatus,
                                  @Param("email") String email,
                                  @Param("address") String address);

    @Modifying
    @Query("UPDATE User u SET " +
            "u.userStatus = :user_status " +
            " WHERE u.telegramId = :telegram_id")

    void updateStatusUserById(
            @Param("telegram_id") long telegramId,
            @Param("user_status") UserStatus userStatus);
}
