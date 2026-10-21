package com.example.demo.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.room.id = :roomId
        AND b.startTime < :requestedEnd
        AND b.endTime > :requestedStart
    """)
    boolean hasOverlappingBooking(
        @Param("roomId") Long roomId,
        @Param("requestedStart") LocalDateTime requestedStart,
        @Param("requestedEnd") LocalDateTime requestedEnd
    );
}
