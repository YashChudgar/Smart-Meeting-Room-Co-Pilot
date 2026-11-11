package com.example.demo.exception;

public class DuplicateRoomCodeException extends RuntimeException{
    public DuplicateRoomCodeException(String message) {
        super(message);
    }
}
