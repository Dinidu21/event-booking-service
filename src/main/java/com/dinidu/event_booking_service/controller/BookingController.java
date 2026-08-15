package com.dinidu.event_booking_service.controller;

import com.dinidu.event_booking_service.entity.Booking;
import com.dinidu.event_booking_service.service.BookingService;
import com.dinidu.event_booking_service.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final EventService eventService;

    public BookingController(
            BookingService bookingService,
            EventService eventService) {

        this.bookingService = bookingService;
        this.eventService = eventService;
    }

    public record BookingRequest(
            Long userId,
            Long ticketTypeId,
            int quantity) {
    }

    public record BookingStatusRequest(
            Booking.Status status) {
    }

    @PostMapping
    public ResponseEntity<Booking> book(
            @RequestBody BookingRequest req) {

        Booking booking = bookingService.book(
                req.userId(),
                req.ticketTypeId(),
                req.quantity());

        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(eventService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(eventService.getBooking(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                eventService.getBookingsByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingStatusRequest request) {

        return ResponseEntity.ok(
                eventService.updateBooking(id, request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id) {

        eventService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}