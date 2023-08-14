package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.model.Report;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.ReportRepository;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Бизнес-логика по работе с отчетами.
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Report saveReport(User userId,
                             LocalDate dateReport,
                             StatusReport statusReport,
                             String reportText,
                             byte[] picture) {

        Report report = new Report(userId,
                dateReport,
                statusReport,
                reportText,
                picture);

        return reportRepository.save(report);
    }

    @Override
    @Transactional
    public void updateReportByUserId(User userId,
                                     LocalDate dateReport,
                                     StatusReport statusReport,
                                     String reportText,
                                     byte[] picture) {


        reportRepository.updateReportById(userId,
                dateReport,
                statusReport,
                reportText,
                picture);
    }

    @Override
    @Transactional
    public void updateDateEndOfProbationById(User userId,
                                             LocalDate dateEndOfProbation) {

        reportRepository.updateDateEndOfProbationById(userId,
                dateEndOfProbation);
    }

    @Override
    @Transactional
    public void updateStatusReportById(User userId,
                                       StatusReport statusReport) {

        reportRepository.updateStatusReportById(userId,
                statusReport);
    }

    @Override
    public void deleteReportById(Long id) {

        reportRepository.deleteById(id);
    }

    @Override
    public Collection<Report> getAllReport() {
        return reportRepository.findAll();
    }
}
