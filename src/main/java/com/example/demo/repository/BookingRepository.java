package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.room.id = :roomId
        AND b.roomReleased = false
        AND b.startTime < :requestedEndWithBuffer
        AND b.endTime > :requestedStartWithBuffer
    """)
    boolean hasOverlappingBooking(
            @Param("roomId") Long roomId,
            @Param("requestedStartWithBuffer") LocalDateTime requestedStartWithBuffer,
            @Param("requestedEndWithBuffer") LocalDateTime requestedEndWithBuffer
    );

    List<Booking> findByCheckedInFalseAndRoomReleasedFalseAndStartTimeLessThanEqual(
            LocalDateTime deadline
    );
}
