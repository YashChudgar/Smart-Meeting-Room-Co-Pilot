package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.RoomRequestDTO;
import com.example.demo.entity.Room;
import com.example.demo.exception.DuplicateRoomCodeException;
import com.example.demo.exception.RoomNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(
            RoomRepository roomRepository,
            BookingRepository bookingRepository) {

        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    // Retrieve all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Retrieve room by ID
    public Room getRoomId(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(
                        "Room not found with ID: " + id));
    }

    // Search available rooms
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
                        room.getId(), startTime.minusMinutes(15), endTime.plusMinutes(15)))
                .toList();
    }

    // Create a new room
    @Transactional
    public Room createRoom(RoomRequestDTO dto) {

        String roomCode = dto.getRoomNumber().trim().toUpperCase();

        if (roomRepository.existsByRoomNumber(roomCode)) {
            throw new DuplicateRoomCodeException(
                    "Room code already exists: " + roomCode);
        }

        Room room = new Room();

        room.setName(dto.getName().trim());
        room.setRoomNumber(roomCode);
        room.setCapacity(dto.getCapacity());
        room.setLocation(dto.getLocation().trim());

        room.setAvailable(
                dto.getAvailable() == null || dto.getAvailable());

        room.setFacilities(dto.getFacilities() == null
                ? new HashSet<>()
                : new HashSet<>(dto.getFacilities()));

        validateFacilities(room.getFacilities());

        room.setCreatedAt(LocalDateTime.now());

        return roomRepository.save(room);
    }

    // Update an existing room
    @Transactional
    public Room updateRoom(Long id, RoomRequestDTO dto) {

        Room room = getRoomId(id);

        String roomCode = dto.getRoomNumber().trim().toUpperCase();

        if (roomRepository.existsByRoomNumberAndIdNot(roomCode, id)) {
            throw new DuplicateRoomCodeException(
                    "Room code already exists: " + roomCode);
        }

        room.setName(dto.getName().trim());
        room.setRoomNumber(roomCode);
        room.setCapacity(dto.getCapacity());
        room.setLocation(dto.getLocation().trim());

        if (dto.getAvailable() != null) {
            room.setAvailable(dto.getAvailable());
        }

        if (dto.getFacilities() != null) {
            Set<String> facilities = new HashSet<>(dto.getFacilities());

            validateFacilities(facilities);

            room.setFacilities(facilities);
        }

        return roomRepository.save(room);
    }

    private void validateFacilities(Set<String> facilities) {

        if (facilities == null) {
            return;
        }

        for (String facility : facilities) {
            if (facility == null || facility.isBlank()) {
                throw new IllegalArgumentException(
                        "Facility names cannot be empty");
            }
        }
    }
}
