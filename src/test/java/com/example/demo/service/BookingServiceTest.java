package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Room;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBookingSuccess() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Main Conference Room");

        Booking booking = new Booking();
        booking.setRoomId(1L);
        booking.setOrganizer("Frank");
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(3));
        booking.setAttendeeCount(4);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking savedBooking = bookingService.createBooking(booking);

        assertNotNull(savedBooking);
        assertEquals("Frank", savedBooking.getOrganizer());
        assertEquals(4, savedBooking.getAttendeeCount());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBookingInvalidTime() {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now().plusHours(3));
        booking.setEndTime(LocalDateTime.now().plusHours(1)); // End is before start!
        booking.setAttendeeCount(2);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Start time must be before end time", exception.getMessage());
    }

    @Test
    void testCreateBookingInvalidAttendeeCount() {
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(3));
        booking.setAttendeeCount(0); // Invalid count

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Attendee count must be at least 1", exception.getMessage());
    }

    @Test
    void testCreateBookingRoomNotFound() {
        Booking booking = new Booking();
        booking.setRoomId(99L); // Non-existent room ID
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(3));
        booking.setAttendeeCount(3);

        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(booking);
        });

        assertEquals("Room not found", exception.getMessage());
    }
}