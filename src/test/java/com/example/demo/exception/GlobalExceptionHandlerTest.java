
package com.example.demo.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleRoomNotFound() {
        RoomNotFoundException exception =
                new RoomNotFoundException("Room not found");

        ResponseEntity<Map<String, String>> response =
                handler.handleRoomNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room Not Found", response.getBody().get("error"));
        assertEquals("Room not found", response.getBody().get("message"));
    }

    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException exception =
                new IllegalArgumentException("Invalid input");

        ResponseEntity<Map<String, String>> response =
                handler.handleIllegalArgument(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Invalid input", response.getBody().get("message"));
    }

    @Test
    void testHandleDuplicateRoomCode() {
        DuplicateRoomCodeException exception =
                new DuplicateRoomCodeException("Room code already exists");

        ResponseEntity<Map<String, String>> response =
                handler.handleDuplicateRoomCode(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody().get("error"));
        assertEquals("Room code already exists",
                response.getBody().get("message"));
    }

    @Test
    void testHandleValidationErrors() {
        BindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "request");

        bindingResult.addError(
                new FieldError("request", "organizer", "Organizer is required"));

        bindingResult.addError(
                new FieldError("request", "roomId", "Room ID is required"));

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response =
                handler.handleValidationErrors(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Organizer is required, Room ID is required",
                response.getBody().get("message"));
    }

    @Test
    void testHandleBookingConflict() {
        BookingConflictException exception =
                new BookingConflictException("Booking time overlaps");

        ResponseEntity<Map<String, String>> response =
                handler.handleBookingConflict(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody().get("error"));
        assertEquals("Booking time overlaps",
                response.getBody().get("message"));
    }

    @Test
    void testHandleBookingCheckIn() {
        BookingCheckInException exception =
                new BookingCheckInException("Check-in is not allowed");

        ResponseEntity<Map<String, String>> response =
                handler.handleBookingCheckIn(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Check-in Conflict", response.getBody().get("error"));
        assertEquals("Check-in is not allowed",
                response.getBody().get("message"));
    }
}
