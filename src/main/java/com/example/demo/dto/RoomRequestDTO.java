package com.example.demo.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RoomRequestDTO {

    @NotBlank(message = "Room name is required")
    @Size(max = 100, message = "Room name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Room code is required")
    @Size(max = 30, message = "Room code cannot exceed 30 characters")
    private String roomNumber;

    @NotNull(message = "Room capacity is required")
    @Positive(message = "Room capacity must be greater than zero")
    private Integer capacity;

    @NotBlank(message = "Room location is required")
    private String location;

    private Set<String> facilities;

    private Boolean available;

    public RoomRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(Set<String> facilities) {
        this.facilities = facilities;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

