package com.dinidu.event_booking_service.controller;

import com.dinidu.event_booking_service.entity.Booking;
import com.dinidu.event_booking_service.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public record BookingRequest(Long userId, Long ticketTypeId, int quantity) {
    }

    @PostMapping
    public ResponseEntity<Booking> book(@RequestBody BookingRequest req) {
        Booking booking = bookingService.book(req.userId(), req.ticketTypeId(), req.quantity());
        return ResponseEntity.ok(booking);
    }
}