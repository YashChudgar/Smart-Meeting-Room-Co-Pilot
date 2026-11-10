package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Room;
import com.example.demo.exception.BookingConflictException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {

        if (booking.getStartTime() == null || booking.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required");
        }

        if (!booking.getStartTime().isBefore(booking.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }

        if (booking.getAttendeeCount() == null ||
                booking.getAttendeeCount() <= 0) {
            throw new IllegalArgumentException(
                    "Attendee count must be at least 1");
        }

        if (booking.getRoom() == null || booking.getRoom().getId() == null) {
            throw new IllegalArgumentException("Room ID is required");
        }

        Long roomId = booking.getRoom().getId();

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        LocalDateTime startTime = booking.getStartTime();
        LocalDateTime endTime = booking.getEndTime();

        // Apply the 15-minute buffer in both directions
        LocalDateTime startWithBuffer = startTime.minusMinutes(15);
        LocalDateTime endWithBuffer = endTime.plusMinutes(15);

        // Check for existing bookings and buffer conflicts
        boolean hasConflict = bookingRepository.hasOverlappingBooking(
                roomId,
                startWithBuffer,
                endWithBuffer);

        if (hasConflict) {
            throw new BookingConflictException(
                    "Room is already booked or conflicts with the 15-minute cleanup buffer");
        }

        booking.setRoom(room);
        booking.setCreatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

}