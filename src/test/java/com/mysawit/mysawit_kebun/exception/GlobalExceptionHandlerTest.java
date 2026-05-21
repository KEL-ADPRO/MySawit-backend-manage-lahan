package com.mysawit.mysawit_kebun.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/kebun/test");
    }

    @Test
    void testHandleKebunNotFound() {
        KebunNotFoundException ex = new KebunNotFoundException("Kebun not found message");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleKebunNotFound(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Kebun Not Found", response.getBody().getError());
        assertEquals("Kebun not found message", response.getBody().getMessage());
        assertEquals("/api/kebun/test", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleKebunOverlap() {
        KebunOverlapException ex = new KebunOverlapException("Kebun overlaps");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleKebunOverlap(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
        assertEquals("Kebun Overlap", response.getBody().getError());
        assertEquals("Kebun overlaps", response.getBody().getMessage());
    }

    @Test
    void testHandleKebunDuplicateName() {
        KebunDuplicateNameException ex = new KebunDuplicateNameException("Name duplicate");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateName(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
        assertEquals("Duplicate Name", response.getBody().getError());
        assertEquals("Name duplicate", response.getBody().getMessage());
    }

    @Test
    void testHandleKebunInvalidOperation() {
        KebunInvalidOperationException ex = new KebunInvalidOperationException("Invalid action");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidOperation(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Invalid Operation", response.getBody().getError());
        assertEquals("Invalid action", response.getBody().getMessage());
    }

    @Test
    void testHandleValidationException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("kebunRequestDto", "nama", "Nama cannot be blank");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Validation Failed", response.getBody().getError());
        assertEquals("Invalid input data", response.getBody().getMessage());
        assertNotNull(response.getBody().getValidationErrors());
        assertEquals("Nama cannot be blank", response.getBody().getValidationErrors().get("nama"));
    }

    @Test
    void testHandleGlobalException() {
        Exception ex = new Exception("Random database error");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("An unexpected error occurred: Random database error", response.getBody().getMessage());
    }
}
