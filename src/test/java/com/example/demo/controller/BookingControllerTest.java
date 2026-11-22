
package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.Booking;
import com.example.demo.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setOrganizer("Yash");
        booking.setAttendeeCount(3);
    }

    @Test
    void testCreateBooking() {

        when(bookingService.createBooking(booking))
                .thenReturn(booking);

        ResponseEntity<Booking> response =
                bookingController.createBooking(booking);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(booking, response.getBody());

        verify(bookingService).createBooking(booking);
    }

    @Test
    void testGetBookingById() {

        when(bookingService.getBookingById(1L))
                .thenReturn(booking);

        ResponseEntity<Booking> response =
                bookingController.getBookingById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(booking, response.getBody());

        verify(bookingService).getBookingById(1L);
    }

    @Test
    void testGetAllBookings() {

        List<Booking> bookings = List.of(booking);

        when(bookingService.getAllBookings())
                .thenReturn(bookings);

        ResponseEntity<List<Booking>> response =
                bookingController.getAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(bookings, response.getBody());

        verify(bookingService).getAllBookings();
    }

    @Test
    void testGetBookingsByOrganizer() {

        List<Booking> bookings = List.of(booking);

        when(bookingService.getBookingsByOrganizer("Yash"))
                .thenReturn(bookings);

        ResponseEntity<List<Booking>> response =
                bookingController.getBookingsByOrganizer("Yash");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(bookings, response.getBody());

        verify(bookingService).getBookingsByOrganizer("Yash");
    }

    @Test
    void testCheckInBooking() {

        when(bookingService.checkInBooking(1L))
                .thenReturn(booking);

        ResponseEntity<Booking> response =
                bookingController.checkInBooking(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(booking, response.getBody());

        verify(bookingService).checkInBooking(1L);
    }
}
