package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Room;
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

        System.out.println("DEBUG - Start Time: " + booking.getStartTime());
        System.out.println("DEBUG - End Time: " + booking.getEndTime());

        if (!booking.getStartTime().isBefore(booking.getEndTime())){
            throw new IllegalArgumentException(
                "Start time must be before end time"
            );
        }

        if (booking.getAttendeeCount() == null || booking.getAttendeeCount() <= 0) {
        throw new IllegalArgumentException(
            "Attendee count must be at least 1"
        );
    }

        Long roomId = booking.getRoom().getId();

        Room room = roomRepository.findById(roomId)
                    .orElseThrow(() ->
                    new IllegalArgumentException("Room not found")
                );

        booking.setRoom(room);

        booking.setCreatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(()->
                new IllegalArgumentException("Booking not found")
            );
    }

}