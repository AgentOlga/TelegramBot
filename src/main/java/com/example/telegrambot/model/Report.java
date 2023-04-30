package com.example.telegrambot.model;

import com.example.telegrambot.constants.ShelterType;
import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.constants.UserStatus;
import com.example.telegrambot.constants.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Сущность отчетов.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "date_report")
    private LocalDate dateReport;

    @Column(name = "status_report")
    private StatusReport statusReport;

    @Column(name = "date_end_of_probation")
    private LocalDate dateEndOfProbation;

    @Column(name = "report_text")
    private String reportText;

    @Column(name = "link_picture")
    private byte[] picture;

    public Report(User userId,
                  LocalDate dateReport,
                  StatusReport statusReport,
                  LocalDate dateEndOfProbation,
                  String reportText,
                  byte[] picture) {
        this.userId = userId;
        this.dateReport = dateReport;
        this.statusReport = statusReport;
        this.dateEndOfProbation = dateEndOfProbation;
        this.reportText = reportText;
        this.picture = picture;
    }

    public Report(LocalDate dateEndOfProbation) {
        this.dateEndOfProbation = dateEndOfProbation;
    }

    public Report(User userId,
                  LocalDate dateReport,
                  StatusReport statusReport,
                  String reportText,
                  byte[] picture) {
        this.userId = userId;
        this.dateReport = dateReport;
        this.statusReport = statusReport;
        this.reportText = reportText;
        this.picture = picture;
    }
}
