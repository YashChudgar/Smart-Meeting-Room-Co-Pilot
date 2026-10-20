package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingRequestDTO {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotBlank(message = "Organizer name is required")
    private String organizer;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    // Constructors
    public BookingRequestDTO() {}

    public BookingRequestDTO(Long roomId, String organizer, LocalDateTime startTime, LocalDateTime endTime) {
        this.roomId = roomId;
        this.organizer = organizer;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}