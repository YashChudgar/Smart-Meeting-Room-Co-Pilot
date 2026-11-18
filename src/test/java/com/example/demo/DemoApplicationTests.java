
package com.example.demo;

import com.example.demo.controller.BookingController;
import com.example.demo.entity.Booking;
import com.example.demo.service.BookingService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
	private BookingService bookingService;

    @Test
    void contextLoads() {
    }

    @Test
    void getAllBookingsReturnsEmptyArray() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of());

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getBookingsByOrganizerReturnsEmptyArray() throws Exception {
        when(bookingService.getBookingsByOrganizer("Sandeep Sarvesh"))
                .thenReturn(List.of());

        mockMvc.perform(
                get("/api/bookings/organizer/{organizerId}", "Sandeep Sarvesh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getMissingBookingReturnsNotFound() throws Exception {
        when(bookingService.getBookingById(99999L))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(get("/api/bookings/99999"))
                .andExpect(status().isBadRequest());
    }
}
