
package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // GET /api/bookings
    // Retrieve all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // GET /api/bookings/organizer/{organizerId}
    // Retrieve bookings for a specific organizer
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Booking>> getBookingsByOrganizer(
            @PathVariable String organizerId) {

        List<Booking> bookings =
                bookingService.getBookingsByOrganizer(organizerId);

        return ResponseEntity.ok(bookings);
    }

    // GET /api/bookings/{id}
    // Retrieve one booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable Long id) {

        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
