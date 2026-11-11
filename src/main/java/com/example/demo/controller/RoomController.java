package com.example.demo.controller;

import com.example.demo.dto.RoomRequestDTO;
import com.example.demo.entity.Room;
import com.example.demo.service.RoomService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Retrieve all rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Retrieve room by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(roomService.getRoomId(id));
    }

    // Search available rooms
    @GetMapping("/available")
    public ResponseEntity<List<Room>> searchAvailableRooms(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime,

            @RequestParam Integer minCapacity) {

        List<Room> rooms = roomService.searchAvailableRooms(
                startTime, endTime, minCapacity);

        return ResponseEntity.ok(rooms);
    }

    // Create a new room
    @PostMapping
    public ResponseEntity<Room> createRoom(
            @Valid @RequestBody RoomRequestDTO dto) {

        Room createdRoom = roomService.createRoom(dto);

        return ResponseEntity.status(201).body(createdRoom);
    }

    // Update an existing room
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequestDTO dto) {

        Room updatedRoom = roomService.updateRoom(id, dto);

        return ResponseEntity.ok(updatedRoom);
    }
}