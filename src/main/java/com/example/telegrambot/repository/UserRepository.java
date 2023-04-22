package com.example.telegrambot.repository;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Выводим пользователя по идентификатору в телеграмм
     * @param userId идентификатор в телеграмм
     * @return найденный пользователь
     */
    //@Modifying
    @Query("SELECT u FROM User u WHERE u.userId = :user_id")
    User findByUserId(@Param("user_id") long userId);

    /**
     * Изменяем пользователя до гостя
     * @param userId идентификатор в телеграмм
     * @param firstName имя гостя
     * @param lastName фамилия гостя
     * @param phoneNumber телефон гостя
     * @param carNumber номер машины гостя
     * @param userType тип пользователя
     * @param userStatus статус пользователя
     */
    //@Modifying
    @Query("UPDATE User u SET u.firstName = :first_name, " +
            "u.lastName = :last_name," +
            "u.phoneNumber = :phone_number," +
            "u.carNumber = :car_number," +
            "u.shelterType = :shelter_type," +
            "u.userType = :user_type," +
            "u.userStatus = :user_status" +
            " where u.userId = :user_id")
    void updateUserInGuestById(@Param("user_id") long userId,
                               @Param("first_name") String firstName,
                               @Param("last_name") String lastName,
                               @Param("phone_number") String phoneNumber,
                               @Param("car_number") String carNumber,
                               @Param("shelter_type") ShelterType shelterType,
                               @Param("user_type") UserType userType,
                               @Param("user_status") UserStatus userStatus);

    /**
     * Изменяем гостя до усыновителя/волонтера
     * @param userId идентификатор в телеграмм
     * @param firstName имя усыновителя/волонтера
     * @param lastName фамилия усыновителя/волонтера
     * @param phoneNumber номер телефона усыновителя/волонтера
     * @param carNumber номер машины усыновителя/волонтера
     * @param userType тип пользователя
     * @param userStatus статус пользователя
     * @param email эл.почта усыновителя/волонтера
     * @param address адрес усыновителя/волонтера
     */
    //@Modifying
    @Query("UPDATE User u SET u.firstName = :first_name, " +
            "u.lastName = :last_name," +
            "u.phoneNumber = :phone_number," +
            "u.carNumber = :car_number," +
            "u.shelterType = :shelter_type," +
            "u.userType = :user_type," +
            "u.userStatus = :user_status," +
            "u.email = :email," +
            "u.address = :address" +
            " where u.userId = :user_id")
    void updateGuestInAdopterById(@Param("user_id") long userId,
                                  @Param("first_name") String firstName,
                                  @Param("last_name") String lastName,
                                  @Param("phone_number") String phoneNumber,
                                  @Param("car_number") String carNumber,
                                  @Param("shelter_type") ShelterType shelterType,
                                  @Param("user_type") UserType userType,
                                  @Param("user_status") UserStatus userStatus,
                                  @Param("email") String email,
                                  @Param("address") String address);

//    @Modifying
//    @Query("SELECT FROM User WHERE userId=(?))
//    Long removeAllLike(@Param(?) long userId);
}
