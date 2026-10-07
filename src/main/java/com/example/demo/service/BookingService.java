package com.example.demo.service;

import java.time.LocalDateTime;

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

    public Booking createBooking(Booking booking) {
        if (!booking.getStartTime().isBefore(booking.getEndTime())){
            throw new IllegalArgumentException(
                "Start time must be before end time"
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
