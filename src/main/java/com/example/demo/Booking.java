
package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomReference;
    private String organizer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public Booking() {
    }

    public Booking(String roomReference, String organizer,
                   LocalDateTime startTime, LocalDateTime endTime,
                   String status) {
        this.roomReference = roomReference;
        this.organizer = organizer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getRoomReference() {
        return roomReference;
    }

    public String getOrganizer() {
        return organizer;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setRoomReference(String roomReference) {
        this.roomReference = roomReference;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
