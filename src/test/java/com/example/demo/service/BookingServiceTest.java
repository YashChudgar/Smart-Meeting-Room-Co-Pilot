
package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Room;
import com.example.demo.exception.BookingCheckInException;
import com.example.demo.exception.BookingConflictException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    private final LocalDateTime start = LocalDateTime.of(2026, 11, 12, 13, 0);

    private final LocalDateTime end = LocalDateTime.of(2026, 11, 12, 14, 0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Room createRoom(Long id) {
        Room room = new Room();
        room.setId(id);
        room.setName("Meeting Room A");
        return room;
    }

    private Booking createBooking(
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        Booking booking = new Booking();
        booking.setRoom(createRoom(roomId));
        booking.setOrganizer("Frank");
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setAttendeeCount(4);

        return booking;
    }

    @Test
    void testCreateBookingSuccess() {

        Room room = createRoom(1L);
        Booking booking = createBooking(1L, start, end);

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(bookingRepository.hasOverlappingBooking(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking savedBooking = bookingService.createBooking(booking);

        assertNotNull(savedBooking);
        assertEquals("Frank", savedBooking.getOrganizer());
        assertEquals(4, savedBooking.getAttendeeCount());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testCreateBookingInvalidTime() {

        Booking booking = createBooking(
                1L, end, start);

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(booking));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testCreateBookingInvalidAttendeeCount() {

        Booking booking = createBooking(1L, start, end);
        booking.setAttendeeCount(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(booking));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testCreateBookingRoomNotFound() {

        Booking booking = createBooking(99L, start, end);

        when(roomRepository.findById(99L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(booking));

        assertEquals("Room not found", exception.getMessage());

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldRejectDirectBookingOverlap() {

        Booking booking = createBooking(
                1L,
                start.plusMinutes(30),
                end.plusMinutes(30));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(createRoom(1L)));

        when(bookingRepository.hasOverlappingBooking(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);

        assertThrows(
                BookingConflictException.class,
                () -> bookingService.createBooking(booking));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldRejectBookingWithin15MinuteBufferAfterExistingBooking() {

        Booking booking = createBooking(
                1L,
                end.plusMinutes(10),
                end.plusHours(1));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(createRoom(1L)));

        when(bookingRepository.hasOverlappingBooking(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);

        assertThrows(
                BookingConflictException.class,
                () -> bookingService.createBooking(booking));

        verify(bookingRepository, never()).save(any(Booking.class));

        verify(bookingRepository).hasOverlappingBooking(
                1L,
                end.plusMinutes(-5),
                end.plusHours(1).plusMinutes(15));
    }

    @Test
    void shouldRejectBookingWithin15MinuteBufferBeforeExistingBooking() {

        Booking booking = createBooking(
                1L,
                start.minusHours(1),
                start.minusMinutes(10));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(createRoom(1L)));

        when(bookingRepository.hasOverlappingBooking(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);

        assertThrows(
                BookingConflictException.class,
                () -> bookingService.createBooking(booking));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldAllowBookingExactly15MinutesAfterPreviousBooking() {

        LocalDateTime newStart = end.plusMinutes(15);
        LocalDateTime newEnd = end.plusHours(1).plusMinutes(15);

        Booking booking = createBooking(1L, newStart, newEnd);

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(createRoom(1L)));

        when(bookingRepository.hasOverlappingBooking(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result);

        verify(bookingRepository).save(any(Booking.class));

        verify(bookingRepository).hasOverlappingBooking(
                1L,
                newStart.minusMinutes(15),
                newEnd.plusMinutes(15));
    }

    @Test
    void shouldAllowSameTimeBookingInDifferentRoom() {

        Booking booking = createBooking(2L, start, end);

        when(roomRepository.findById(2L))
                .thenReturn(Optional.of(createRoom(2L)));

        when(bookingRepository.hasOverlappingBooking(
                eq(2L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result);
        assertEquals(2L, result.getRoom().getId());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void shouldSuccessfullyCheckInBooking() {

        LocalDateTime meetingStart = LocalDateTime.now().plusMinutes(5);

        Booking booking = createBooking(
                1L,
                meetingStart,
                meetingStart.plusHours(1));

        booking.setId(1L);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.checkInBooking(1L);

        assertTrue(result.isCheckedIn());
        assertNotNull(result.getCheckedInAt());

        verify(bookingRepository).save(booking);
    }

    @Test
    void shouldRejectCheckInTooEarly() {

        LocalDateTime meetingStart = LocalDateTime.now().plusMinutes(20);

        Booking booking = createBooking(
                1L,
                meetingStart,
                meetingStart.plusHours(1));

        booking.setId(1L);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        assertThrows(
                BookingCheckInException.class,
                () -> bookingService.checkInBooking(1L));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldRejectCheckInAfter15MinuteDeadline() {

        LocalDateTime meetingStart = LocalDateTime.now().minusMinutes(20);

        Booking booking = createBooking(
                1L,
                meetingStart,
                meetingStart.plusHours(1));

        booking.setId(1L);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        assertThrows(
                BookingCheckInException.class,
                () -> bookingService.checkInBooking(1L));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldRejectDuplicateCheckIn() {

        LocalDateTime meetingStart = LocalDateTime.now().plusMinutes(5);

        Booking booking = createBooking(
                1L,
                meetingStart,
                meetingStart.plusHours(1));

        booking.setId(1L);
        booking.setCheckedIn(true);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        assertThrows(
                BookingCheckInException.class,
                () -> bookingService.checkInBooking(1L));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldSaveCheckInTimestamp() {

        LocalDateTime meetingStart = LocalDateTime.now().plusMinutes(5);

        Booking booking = createBooking(
                1L,
                meetingStart,
                meetingStart.plusHours(1));

        booking.setId(1L);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime beforeCheckIn = LocalDateTime.now();

        Booking result = bookingService.checkInBooking(1L);

        LocalDateTime afterCheckIn = LocalDateTime.now();

        assertNotNull(result.getCheckedInAt());

        assertFalse(result.getCheckedInAt().isBefore(beforeCheckIn));
        assertFalse(result.getCheckedInAt().isAfter(afterCheckIn));

        assertTrue(result.isCheckedIn());
    }

    @Test
    void shouldReleaseNoShowBookingAfter15MinuteDeadline() {

        Booking booking = new Booking();

        when(bookingRepository
                .findByCheckedInFalseAndRoomReleasedFalseAndStartTimeLessThanEqual(
                        any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        int releasedCount = bookingService.releaseNoShowBookings();

        assertEquals(1, releasedCount);
        assertTrue(booking.isNoShow());
        assertTrue(booking.isRoomReleased());
        assertNotNull(booking.getReleasedAt());

        verify(bookingRepository).saveAll(List.of(booking));
    }

    @Test
    void shouldNotReleaseCheckedInBookings() {

        Booking checkedInBooking = new Booking();
        checkedInBooking.setCheckedIn(true);

        when(bookingRepository
                .findByCheckedInFalseAndRoomReleasedFalseAndStartTimeLessThanEqual(
                        any(LocalDateTime.class)))
                .thenReturn(List.of());

        int releasedCount = bookingService.releaseNoShowBookings();

        assertEquals(0, releasedCount);
        assertFalse(checkedInBooking.isNoShow());
        assertFalse(checkedInBooking.isRoomReleased());

        verify(bookingRepository).saveAll(List.of());
    }

    @Test
    void shouldReleaseMultipleNoShowBookings() {

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        Booking booking3 = new Booking();

        List<Booking> overdueBookings = List.of(
                booking1,
                booking2,
                booking3);

        when(bookingRepository
                .findByCheckedInFalseAndRoomReleasedFalseAndStartTimeLessThanEqual(
                        any(LocalDateTime.class)))
                .thenReturn(overdueBookings);

        int releasedCount = bookingService.releaseNoShowBookings();

        assertEquals(3, releasedCount);

        for (Booking booking : overdueBookings) {
            assertTrue(booking.isNoShow());
            assertTrue(booking.isRoomReleased());
            assertNotNull(booking.getReleasedAt());
        }

        verify(bookingRepository).saveAll(overdueBookings);
    }

    @Test
    void shouldNotReleaseBookingsWhenNoOverdueBookingsExist() {

        when(bookingRepository
                .findByCheckedInFalseAndRoomReleasedFalseAndStartTimeLessThanEqual(
                        any(LocalDateTime.class)))
                .thenReturn(List.of());

        int releasedCount = bookingService.releaseNoShowBookings();

        assertEquals(0, releasedCount);

        verify(bookingRepository).saveAll(List.of());
    }
}
