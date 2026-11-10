package com.example.demo.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRoomNotFound(
            RoomNotFoundException exception) {
        Map<String, String> response = Map.of(
                "error", "Room Not Found",
                "message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException exception) {

        Map<String, String> response = Map.of(
                "error", "Bad Request",
                "message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


@ExceptionHandler(DuplicateRoomCodeException.class)
public ResponseEntity<Map<String, String>> handleDuplicateRoomCode(
        DuplicateRoomCodeException exception) {

    Map<String, String> response = Map.of(
            "error", "Conflict",
            "message", exception.getMessage()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException exception) {

    String message = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    Map<String, String> response = Map.of(
            "error", "Bad Request",
            "message", message
    );

    return ResponseEntity.badRequest().body(response);
}

@ExceptionHandler(BookingConflictException.class)
public ResponseEntity<Map<String, String>> handleBookingConflict(
        BookingConflictException exception) {

    Map<String, String> response = Map.of(
            "error", "Conflict",
            "message", exception.getMessage()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
}

}
