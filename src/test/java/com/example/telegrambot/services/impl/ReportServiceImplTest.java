package com.example.telegrambot.services.impl;

import com.example.telegrambot.constants.StatusReport;
import com.example.telegrambot.model.Report;
import com.example.telegrambot.model.User;
import com.example.telegrambot.repository.ReportRepository;
import com.example.telegrambot.repository.UserRepository;
import com.example.telegrambot.services.ReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    private ReportService reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reportService = new ReportServiceImpl(reportRepository, userRepository);
    }

    @Test
    void testSaveReport() {
        User user = new User();
        LocalDate date = LocalDate.now();
        StatusReport status = StatusReport.DEFAULT;
        String reportText = "Test report";
        byte[] picture = new byte[10];

        Report report = new Report(user, date, status, reportText, picture);
        when(reportRepository.save(report)).thenReturn(report);

        Report savedReport = reportService.saveReport(user, date, status, reportText, picture);

        Assertions.assertNotNull(savedReport);
        Assertions.assertEquals(report, savedReport);
        verify(reportRepository, times(1)).save(report);
    }

    @Test
    void testUpdateReportByUserId() {
        User user = new User();
        LocalDate date = LocalDate.now();
        StatusReport status = StatusReport.DEFAULT;
        String reportText = "Test report";
        byte[] picture = new byte[10];

        reportService.updateReportByUserId(user, date, status, reportText, picture);
        verify(reportRepository, times(1)).updateReportById(user, date, status, reportText, picture);
    }

    @Test
    void testUpdateDateEndOfProbationById() {
        User user = new User();
        LocalDate date = LocalDate.now();

        reportService.updateDateEndOfProbationById(user, date);
        verify(reportRepository, times(1)).updateDateEndOfProbationById(user, date);
    }

    @Test
    void testUpdateStatusReportById() {
        User user = new User();
        StatusReport status = StatusReport.DEFAULT;

        reportService.updateStatusReportById(user, status);
        verify(reportRepository, times(1)).updateStatusReportById(user, status);
    }

    @Test
    void testDeleteReportById() {
        Long id = 1L;

        reportService.deleteReportById(id);
        verify(reportRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllReport() {
        List<Report> reports = new ArrayList<>();
        reports.add(new Report());
        reports.add(new Report());

        when(reportRepository.findAll()).thenReturn(reports);

        Collection<Report> allReports = reportService.getAllReport();

        Assertions.assertNotNull(allReports);
        Assertions.assertEquals(reports, allReports);
        verify(reportRepository, times(1)).findAll();
    }

}