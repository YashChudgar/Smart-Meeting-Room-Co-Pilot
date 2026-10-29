
package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Spring Boot 4 import
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    private Booking sampleBooking() {
        return new Booking(
                "ROOM-101",
                "Sandeep Sarvesh",
                LocalDateTime.parse("2026-11-02T10:00:00"),
                LocalDateTime.parse("2026-11-02T11:00:00"),
                "CONFIRMED"
        );
    }

    @Test
    void contextLoads() {
    }

    @Test
    void getAllBookingsReturnsBookings() throws Exception {
        when(bookingService.getAllBookings())
                .thenReturn(List.of(sampleBooking()));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomReference").value("ROOM-101"))
                .andExpect(jsonPath("$[0].organizer").value("Sandeep Sarvesh"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    @Test
    void getAllBookingsReturnsEmptyArray() throws Exception {
        when(bookingService.getAllBookings())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getBookingsByOrganizerReturnsMatchingBooking() throws Exception {
        when(bookingService.getBookingsByOrganizer("Sandeep Sarvesh"))
                .thenReturn(List.of(sampleBooking()));

        mockMvc.perform(
                get("/api/bookings/organizer/{organizerId}", "Sandeep Sarvesh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizer").value("Sandeep Sarvesh"));
    }

    @Test
    void getExistingBookingReturnsBooking() throws Exception {
        when(bookingService.getBookingById(1L))
                .thenReturn(Optional.of(sampleBooking()));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomReference").value("ROOM-101"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void getMissingBookingReturns404() throws Exception {
        when(bookingService.getBookingById(99999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/99999"))
                .andExpect(status().isNotFound());
    }
}
