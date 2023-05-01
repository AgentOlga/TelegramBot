package com.example.telegrambot.repository;

import com.example.telegrambot.model.Dialog;
import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {

    @Query("SELECT d FROM Dialog d WHERE d.guestId = :guest_id")
    Dialog findDialogByUserId(@Param("guest_id") User guestId);
}
