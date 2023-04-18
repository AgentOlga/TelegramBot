package com.example.telegrambot.repository;

import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean findAllByUserId(long userId);

//    @Modifying
//    @Query("SELECT FROM User WHERE userId=(?))
//    Long removeAllLike(@Param(?) long userId);
}
