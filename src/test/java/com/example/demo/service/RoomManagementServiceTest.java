
package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.RoomRequestDTO;
import com.example.demo.entity.Room;
import com.example.demo.exception.DuplicateRoomCodeException;
import com.example.demo.exception.RoomNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
class RoomManagementServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private RoomService roomService;

    private RoomRequestDTO createRequest() {
        RoomRequestDTO request = new RoomRequestDTO();

        request.setName("Meeting Room B");
        request.setRoomNumber("B101");
        request.setCapacity(8);
        request.setLocation("Second Floor");
        request.setFacilities(
                Set.of("Projector", "Whiteboard"));
        request.setAvailable(true);

        return request;
    }

    @Test
    void shouldCreateRoomSuccessfully() {

        RoomRequestDTO request = createRequest();

        when(roomRepository.existsByRoomNumber("B101"))
                .thenReturn(false);

        when(roomRepository.save(any(Room.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Room result = roomService.createRoom(request);

        assertNotNull(result);
        assertEquals("Meeting Room B", result.getName());
        assertEquals("B101", result.getRoomNumber());
        assertEquals(8, result.getCapacity());
        assertTrue(result.isAvailable());
        assertEquals(2, result.getFacilities().size());

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void shouldRejectDuplicateRoomCodeDuringCreation() {

        RoomRequestDTO request = createRequest();

        when(roomRepository.existsByRoomNumber("B101"))
                .thenReturn(true);

        assertThrows(
                DuplicateRoomCodeException.class,
                () -> roomService.createRoom(request)
        );

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void shouldUpdateRoomSuccessfully() {

        Room existingRoom = new Room();
        existingRoom.setId(6L);
        existingRoom.setName("Old Room");
        existingRoom.setRoomNumber("B101");
        existingRoom.setCapacity(4);
        existingRoom.setLocation("First Floor");

        when(roomRepository.findById(6L))
                .thenReturn(Optional.of(existingRoom));

        when(roomRepository.existsByRoomNumberAndIdNot("B101", 6L))
                .thenReturn(false);

        when(roomRepository.save(any(Room.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RoomRequestDTO request = createRequest();

        Room result = roomService.updateRoom(6L, request);

        assertEquals("Meeting Room B", result.getName());
        assertEquals("B101", result.getRoomNumber());
        assertEquals(8, result.getCapacity());
        assertEquals("Second Floor", result.getLocation());

        verify(roomRepository).save(existingRoom);
    }

    @Test
    void shouldRejectDuplicateRoomCodeDuringUpdate() {

        Room existingRoom = new Room();
        existingRoom.setId(6L);

        when(roomRepository.findById(6L))
                .thenReturn(Optional.of(existingRoom));

        when(roomRepository.existsByRoomNumberAndIdNot("B101", 6L))
                .thenReturn(true);

        assertThrows(
                DuplicateRoomCodeException.class,
                () -> roomService.updateRoom(6L, createRequest())
        );

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void shouldAllowRoomToKeepItsOwnRoomCode() {

        Room existingRoom = new Room();
        existingRoom.setId(6L);
        existingRoom.setRoomNumber("B101");

        when(roomRepository.findById(6L))
                .thenReturn(Optional.of(existingRoom));

        when(roomRepository.existsByRoomNumberAndIdNot("B101", 6L))
                .thenReturn(false);

        when(roomRepository.save(any(Room.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Room result = roomService.updateRoom(6L, createRequest());

        assertEquals("B101", result.getRoomNumber());
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentRoom() {

        when(roomRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RoomNotFoundException.class,
                () -> roomService.updateRoom(999L, createRequest())
        );

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void shouldRejectBlankFacilityNames() {

        RoomRequestDTO request = createRequest();
        request.setFacilities(Set.of(" "));

        when(roomRepository.existsByRoomNumber("B101"))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> roomService.createRoom(request)
        );

        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void shouldRetrieveAllRooms() {

        Room room = new Room();
        room.setId(1L);
        room.setName("Meeting Room A");

        when(roomRepository.findAll())
                .thenReturn(List.of(room));

        List<Room> result = roomService.getAllRooms();

        assertEquals(1, result.size());
        assertEquals("Meeting Room A", result.get(0).getName());
    }
}
