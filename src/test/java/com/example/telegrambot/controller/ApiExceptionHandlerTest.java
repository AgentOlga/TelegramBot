package com.example.telegrambot.controller;

import com.example.telegrambot.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @Mock
    private ValidationException validationException;

    @Mock
    private NotFoundAnimalException notFoundAnimalException;

    @Mock
    private NotFoundAdopterException notFoundAdopterException;

    @Mock
    private NotFoundUserException notFoundUserException;

    @Mock
    private NotFoundReportException notFoundReportException;

    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void handlerValidationException_ReturnsBadRequest() {
        String message = "Validation exception";

        when(validationException.getMessage()).thenReturn(message);

        ResponseEntity<String> result = apiExceptionHandler.handlerValidationException(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(message, result.getBody());
    }

    @Test
    public void handlerNotFoundAnimalException_ReturnsBadRequest() {
        String message = "Not found animal exception";

        when(notFoundAnimalException.getMessage()).thenReturn(message);

        ResponseEntity<String> result = apiExceptionHandler.handlerNotFoundAnimalException(notFoundAnimalException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(message, result.getBody());
    }

    @Test
    public void handlerNotFoundAdopterException_ReturnsBadRequest() {
        String message = "Not found adopter exception";

        when(notFoundAdopterException.getMessage()).thenReturn(message);

        ResponseEntity<String> result = apiExceptionHandler.handlerNotFoundAdopterException(notFoundAdopterException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(message, result.getBody());
    }

    @Test
    public void handlerNotFoundUserException_ReturnsBadRequest() {
        String message = "Not found user exception";

        when(notFoundUserException.getMessage()).thenReturn(message);

        ResponseEntity<String> result = apiExceptionHandler.handlerNotFoundUserException(notFoundUserException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(message, result.getBody());
    }

    @Test
    public void handlerNotFoundReportException_ReturnsBadRequest() {
        String message = "Not found report exception";

        when(notFoundReportException.getMessage()).thenReturn(message);

        ResponseEntity<String> result = apiExceptionHandler.handlerNotFoundReportException(notFoundReportException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(message, result.getBody());
    }

}