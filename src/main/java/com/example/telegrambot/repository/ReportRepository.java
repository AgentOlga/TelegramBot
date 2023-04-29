package com.example.telegrambot.repository;

import com.example.telegrambot.constants.Color;
import com.example.telegrambot.constants.PetType;
import com.example.telegrambot.constants.Sex;
import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.model.Report;
import com.example.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.userId = :user_id")
    Report findReportByUserId(@Param("user_id") User user);

    @Modifying
    @Query("UPDATE Report r SET " +
            "r.dateReport = :date_report," +
            "r.statusReport = :status_report," +
            "r.reportText = :report_text," +
            "r.picture = :picture" +
            " WHERE r.userId = :user_id")

    void updateReportById(
            @Param("user_id") User user,
            @Param("date_report") LocalDate dateReport,
            @Param("status_report") StatusReport statusReport,
            @Param("report_text") String reportText,
            @Param("picture") byte[] picture);

    @Modifying
    @Query("UPDATE Report r SET " +
            "r.dateEndOfProbation = :date_end_of_probation " +
            " WHERE r.userId = :user_id")

    void updateDateEndOfProbationById(
            @Param("user_id") User user,
            @Param("date_end_of_probation") LocalDate dateEndOfProbation);
}