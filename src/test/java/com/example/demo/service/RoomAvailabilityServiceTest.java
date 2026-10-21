package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.exception.RoomNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomAvailabilityServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("Meeting Room A");
        room.setRoomNumber("A101");
        room.setCapacity(4);
        room.setLocation("First Floor");
        room.setAvailable(true);

        startTime = LocalDateTime.of(2026, 10, 25, 10, 0);
        endTime = LocalDateTime.of(2026, 10, 25, 11, 0);
    }

    @Test
    void shouldReturnRoomWhenAvailableAndCapacityIsSufficient() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        when(bookingRepository.hasOverlappingBooking(
                1L, startTime, endTime)).thenReturn(false);

        List<Room> result = roomService.searchAvailableRooms(
                startTime, endTime, 4);

        assertEquals(1, result.size());
        assertEquals("Meeting Room A", result.get(0).getName());

        verify(roomRepository).findAll();
        verify(bookingRepository).hasOverlappingBooking(
                1L, startTime, endTime);
    }

    @Test
    void shouldExcludeRoomWhenBookingOverlaps() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        when(bookingRepository.hasOverlappingBooking(
                1L, startTime, endTime)).thenReturn(true);

        List<Room> result = roomService.searchAvailableRooms(
                startTime, endTime, 4);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldExcludeRoomWhenCapacityIsInsufficient() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<Room> result = roomService.searchAvailableRooms(
                startTime, endTime, 5);

        assertTrue(result.isEmpty());

        verify(bookingRepository, never())
                .hasOverlappingBooking(anyLong(), any(), any());
    }

    @Test
    void shouldExcludeRoomWhenMarkedUnavailable() {
        room.setAvailable(false);

        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<Room> result = roomService.searchAvailableRooms(
                startTime, endTime, 1);

        assertTrue(result.isEmpty());

        verify(bookingRepository, never())
                .hasOverlappingBooking(anyLong(), any(), any());
    }

    @Test
    void shouldReturnEmptyListWhenNoRoomsExist() {
        when(roomRepository.findAll()).thenReturn(List.of());

        List<Room> result = roomService.searchAvailableRooms(
                startTime, endTime, 1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRejectStartTimeAfterEndTime() {
        LocalDateTime invalidStart =
                LocalDateTime.of(2026, 10, 25, 12, 0);

        LocalDateTime invalidEnd =
                LocalDateTime.of(2026, 10, 25, 11, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roomService.searchAvailableRooms(
                        invalidStart, invalidEnd, 4)
        );

        assertEquals(
                "Start time must be before end time",
                exception.getMessage()
        );

        verifyNoInteractions(roomRepository, bookingRepository);
    }

    @Test
    void shouldRejectMinimumCapacityBelowOne() {
        assertThrows(
                IllegalArgumentException.class,
                () -> roomService.searchAvailableRooms(
                        startTime, endTime, 0)
        );

        verifyNoInteractions(roomRepository, bookingRepository);
    }
}
