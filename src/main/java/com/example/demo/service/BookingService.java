package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Room;
import com.example.demo.exception.BookingCheckInException;
import com.example.demo.exception.BookingConflictException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

import jakarta.transaction.Transactional;

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
    public List<Booking> getBookingsByOrganizer(String organizer) {
    return bookingRepository.findByOrganizer(organizer);
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

    public Booking checkInBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime checkInOpensAt = booking.getStartTime().minusMinutes(10);
        LocalDateTime checkInDeadline = booking.getStartTime().plusMinutes(15);

        // Reject check-in more than 10 minutes before the meeting
        if (now.isBefore(checkInOpensAt)) {
            throw new BookingCheckInException(
                    "Check-in is only available 10 minutes before the meeting");
        }

        // Reject check-in after the 15-minute grace period
        if (now.isAfter(checkInDeadline)) {
            throw new BookingCheckInException(
                    "Check-in period has expired");
        }

        // Prevent duplicate check-ins
        if (booking.isCheckedIn()) {
            throw new BookingCheckInException(
                    "This booking has already been checked in");
        }

        booking.setCheckedIn(true);
        booking.setCheckedInAt(now);

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    @Transactional 
    public int releaseNoShowBookings() {

        LocalDateTime deadline = LocalDateTime.now().minusMinutes(15);

        List<Booking> overdueBookings = bookingRepository
                .findByCheckedInFalseAndRoomReleasedFalseAndStartTimeLessThanEqual(
                        deadline);

        LocalDateTime releasedTime = LocalDateTime.now();

        for (Booking booking : overdueBookings) {

            booking.setNoShow(true);
            booking.setRoomReleased(true);
            booking.setReleasedAt(releasedTime);
        }

        bookingRepository.saveAll(overdueBookings);

        return overdueBookings.size();
    }

}