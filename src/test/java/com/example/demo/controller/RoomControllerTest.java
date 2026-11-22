
package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.RoomRequestDTO;
import com.example.demo.entity.Room;
import com.example.demo.service.RoomService;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

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
    void testGetAllRooms() {

        List<Room> rooms = List.of(room);

        when(roomService.getAllRooms()).thenReturn(rooms);

        ResponseEntity<List<Room>> response =
                roomController.getAllRooms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(rooms, response.getBody());

        verify(roomService).getAllRooms();
    }

    @Test
    void testGetRoomById() {

        when(roomService.getRoomId(1L)).thenReturn(room);

        ResponseEntity<Room> response =
                roomController.getRoomById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(room, response.getBody());

        verify(roomService).getRoomId(1L);
    }

    @Test
    void testSearchAvailableRooms() {

        LocalDateTime startTime =
                LocalDateTime.of(2026, 11, 25, 10, 0);

        LocalDateTime endTime =
                LocalDateTime.of(2026, 11, 25, 11, 0);

        List<Room> rooms = List.of(room);

        when(roomService.searchAvailableRooms(
                startTime, endTime, 4)).thenReturn(rooms);

        ResponseEntity<List<Room>> response =
                roomController.searchAvailableRooms(
                        startTime, endTime, 4);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(rooms, response.getBody());

        verify(roomService).searchAvailableRooms(
                startTime, endTime, 4);
    }

    @Test
    void testCreateRoom() {

        RoomRequestDTO dto = new RoomRequestDTO();

        dto.setName("Meeting Room A");
        dto.setRoomNumber("A101");
        dto.setCapacity(4);
        dto.setLocation("First Floor");

        when(roomService.createRoom(dto)).thenReturn(room);

        ResponseEntity<Room> response =
                roomController.createRoom(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(room, response.getBody());

        verify(roomService).createRoom(dto);
    }

    @Test
    void testUpdateRoom() {

        RoomRequestDTO dto = new RoomRequestDTO();

        dto.setName("Updated Meeting Room");
        dto.setRoomNumber("A101");
        dto.setCapacity(6);
        dto.setLocation("Second Floor");

        when(roomService.updateRoom(1L, dto)).thenReturn(room);

        ResponseEntity<Room> response =
                roomController.updateRoom(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(room, response.getBody());

        verify(roomService).updateRoom(1L, dto);
    }
}
