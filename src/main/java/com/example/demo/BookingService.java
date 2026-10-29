
package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Retrieve all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Retrieve bookings for a particular organizer
    public List<Booking> getBookingsByOrganizer(String organizer) {
        return bookingRepository.findByOrganizer(organizer);
    }

    // Retrieve one booking by its ID
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
}
