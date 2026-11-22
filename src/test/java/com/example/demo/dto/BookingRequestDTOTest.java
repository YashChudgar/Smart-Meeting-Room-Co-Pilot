
package com.example.demo.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class BookingRequestDTOTest {

    @Test
    void testNoArgsConstructorAndSettersAndGetters() {

        BookingRequestDTO dto = new BookingRequestDTO();

        LocalDateTime startTime =
                LocalDateTime.of(2026, 11, 25, 10, 0);

        LocalDateTime endTime =
                LocalDateTime.of(2026, 11, 25, 11, 0);

        // Verify default values
        assertNull(dto.getRoomId());
        assertNull(dto.getOrganizer());
        assertNull(dto.getStartTime());
        assertNull(dto.getEndTime());

        // Set values
        dto.setRoomId(1L);
        dto.setOrganizer("Yash");
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);

        // Verify values
        assertEquals(1L, dto.getRoomId());
        assertEquals("Yash", dto.getOrganizer());
        assertEquals(startTime, dto.getStartTime());
        assertEquals(endTime, dto.getEndTime());
    }

    @Test
    void testParameterizedConstructor() {

        LocalDateTime startTime =
                LocalDateTime.of(2026, 11, 25, 10, 0);

        LocalDateTime endTime =
                LocalDateTime.of(2026, 11, 25, 11, 0);

        BookingRequestDTO dto = new BookingRequestDTO(
                1L,
                "Yash",
                startTime,
                endTime
        );

        assertEquals(1L, dto.getRoomId());
        assertEquals("Yash", dto.getOrganizer());
        assertEquals(startTime, dto.getStartTime());
        assertEquals(endTime, dto.getEndTime());
    }
}
