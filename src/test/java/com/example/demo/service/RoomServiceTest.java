package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.exception.RoomNotFoundException;
import com.example.demo.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("Meeting Room A");
        room.setRoomNumber("A101");
        room.setCapacity(4);
        room.setLocation("First Floor");
        room.setAvailable(true);
    }

    @Test
    void getAllRoomsShouldReturnRooms() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<Room> rooms = roomService.getAllRooms();

        assertEquals(1, rooms.size());
        assertEquals("Meeting Room A", rooms.get(0).getName());
        verify(roomRepository).findAll();
    }

    @Test
    void getAllRoomsShouldReturnEmptyListWhenNoRoomsExist() {
        when(roomRepository.findAll()).thenReturn(List.of());

        List<Room> rooms = roomService.getAllRooms();

        assertNotNull(rooms);
        assertTrue(rooms.isEmpty());
        verify(roomRepository).findAll();
    }

    @Test
    void getRoomByIdShouldReturnRoomWhenItExists() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomId(1L);

        assertEquals(1L, result.getId());
        assertEquals("Meeting Room A", result.getName());
        assertEquals(4, result.getCapacity());
        verify(roomRepository).findById(1L);
    }

    @Test
    void getRoomByIdShouldThrowExceptionWhenRoomDoesNotExist() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(
                RoomNotFoundException.class,
                () -> roomService.getRoomId(999L)
        );

        assertEquals("Room not found with ID: 999", exception.getMessage());
        verify(roomRepository).findById(999L);
    }
}