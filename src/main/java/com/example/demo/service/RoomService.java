package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Room;
import com.example.demo.exception.RoomNotFoundException;
import com.example.demo.repository.RoomRepository;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomId(Long id) {
        return roomRepository.findById(id)
            .orElseThrow(()->
                new RoomNotFoundException(
                    "Room not found with ID: " + id
                )
        );
    }
}
