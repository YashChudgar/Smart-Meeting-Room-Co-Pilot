
package com.example.demo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NoShowDetectionScheduler {

    private final BookingService bookingService;

    public NoShowDetectionScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 60000)
    public void detectNoShowBookings() {

        int releasedCount = bookingService.releaseNoShowBookings();

        if (releasedCount > 0) {
            System.out.println(
                    releasedCount + " no-show booking(s) detected and room(s) released."
            );
        }
    }
}
