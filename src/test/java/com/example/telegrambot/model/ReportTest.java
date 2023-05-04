package com.example.telegrambot.model;

import com.example.telegrambot.constants.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    private static final LocalDate CORRECT_DATA_REPORT = LocalDate.now();
    private static final LocalDate CORRECT_DATA = LocalDate.now().plusMonths(1);
    private static final StatusReport CORRECT_STATUS_REPORT = StatusReport.DEFAULT;
    private static final String CORRECT_TEXT = "really well";
    private static final byte[] CORRECT_PICTURE = {'1', '2', '3', '4'};
    private static final long CORRECT_USER_ID = 123456789;
    private static final String CORRECT_USER_NAME = "Nick";
    private static final UserType CORRECT_USER_TYPE = UserType.DEFAULT;
    private static final UserStatus CORRECT_USER_STATUS = UserStatus.APPROVE;
    private static final String CORRECT_FIRST_NAME = "Ivan";
    private static final String CORRECT_LAST_NAME = "Ivanov";
    private static final String CORRECT_PHONE_NUMBER = "1234567891";
    private static final String CORRECT_CAR_NUMBER = "123456";
    private static final ShelterType CORRECT_SHELTER_TYPE = ShelterType.DOG_SHELTER;
    private static final String CORRECT_MAIL = "Ivan@mail.ru";
    private static final String CORRECT_ADDRESS = "Astana, lenina 12";

    private Report report;
    private Report reportCorrect;
    private User adopter;


    @BeforeEach
    public void initTest() {
        reportCorrect = new Report(adopter,
                CORRECT_DATA_REPORT,
                CORRECT_STATUS_REPORT,
                CORRECT_DATA,
                CORRECT_TEXT,
                CORRECT_PICTURE);

        report = new Report(adopter,
                CORRECT_DATA_REPORT,
                CORRECT_STATUS_REPORT,
                CORRECT_DATA,
                CORRECT_TEXT,
                CORRECT_PICTURE);

        adopter = new User(CORRECT_USER_ID, CORRECT_USER_NAME, CORRECT_FIRST_NAME,
                CORRECT_LAST_NAME, CORRECT_PHONE_NUMBER, CORRECT_CAR_NUMBER,
                CORRECT_MAIL, CORRECT_ADDRESS,
                CORRECT_SHELTER_TYPE, CORRECT_USER_TYPE, CORRECT_USER_STATUS);

    }

    @AfterEach
    public void afterTest() {
        System.out.println("Testing is finished!");
    }

    @Test
    public void checkingForIncomingCorrectDataFromTheMethodAddUser() {
        assertEquals(reportCorrect, report);
    }

    @Test
    void testConstructorWithAllArgs() {
        //Arrange
        User user = adopter;
        LocalDate dateReport = CORRECT_DATA_REPORT;
        StatusReport statusReport = CORRECT_STATUS_REPORT;
        LocalDate dateEndOfProbation = CORRECT_DATA;
        String reportText = CORRECT_TEXT;
        byte[] picture = CORRECT_PICTURE;

        //Act
        Report report = new Report(user,
                dateReport,
                statusReport,
                dateEndOfProbation,
                reportText,
                picture);

        //Assert
        assertNotNull(report);
        assertEquals(user, report.getUserId());
        assertEquals(dateReport, report.getDateReport());
        assertEquals(statusReport, report.getStatusReport());
        assertEquals(dateEndOfProbation, report.getDateEndOfProbation());
        assertEquals(reportText, report.getReportText());
        assertEquals(picture, report.getPicture());
    }

    @Test
    void testConstructorWithDateEndOfProbation() {
        //Arrange
        LocalDate dateEndOfProbation = CORRECT_DATA;

        //Act
        Report report = new Report(dateEndOfProbation);

        //Assert
        assertNotNull(report);
        assertNull(report.getUserId());
        assertNull(report.getDateReport());
        assertNull(report.getStatusReport());
        assertEquals(dateEndOfProbation, report.getDateEndOfProbation());
        assertNull(report.getReportText());
        assertNull(report.getPicture());
    }

    @Test
    void testConstructorWithNoDateEndOfProbation() {
        //Act
        Report report = new Report(null);

        //Assert
        assertNotNull(report);
        assertNull(report.getDateEndOfProbation());
    }

    @Test
    void testConstructorWithNoPicture() {
        //Arrange
        User user = adopter;
        LocalDate dateReport = CORRECT_DATA_REPORT;
        StatusReport statusReport = CORRECT_STATUS_REPORT;
        String reportText = CORRECT_TEXT;

        //Act
        Report report = new Report(user, dateReport, statusReport, reportText, null);

        //Assert
        assertNotNull(report);
        assertEquals(user, report.getUserId());
        assertEquals(dateReport, report.getDateReport());
        assertEquals(statusReport, report.getStatusReport());
        assertNull(report.getPicture());
    }
}
