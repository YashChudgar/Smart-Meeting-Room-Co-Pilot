package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Room;
import com.example.demo.exception.RoomNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomId(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(
                        "Room not found with ID: " + id));
    }

    public List<Room> searchAvailableRooms(
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer minCapacity) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }

        if (minCapacity == null || minCapacity < 1) {
            throw new IllegalArgumentException(
                    "Minimum capacity must be at least 1");
        }

        return roomRepository.findAll()
                .stream()
                .filter(Room::isAvailable)
                .filter(room -> room.getCapacity() != null
                        && room.getCapacity() >= minCapacity)
                .filter(room -> !bookingRepository.hasOverlappingBooking(
                        room.getId(), startTime, endTime))
                .toList();
    }
}
