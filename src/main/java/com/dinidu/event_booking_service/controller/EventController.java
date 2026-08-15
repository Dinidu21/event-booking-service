package com.dinidu.event_booking_service.controller;

import com.dinidu.event_booking_service.entity.Event;
import com.dinidu.event_booking_service.entity.TicketType;
import com.dinidu.event_booking_service.entity.Venue;
import com.dinidu.event_booking_service.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // ---------------- VENUES ----------------

    @PostMapping("/venues")
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        return ResponseEntity.ok(eventService.createVenue(venue));
    }

    @GetMapping("/venues")
    public ResponseEntity<List<Venue>> getVenues() {
        return ResponseEntity.ok(eventService.getAllVenues());
    }

    @GetMapping("/venues/{id}")
    public ResponseEntity<Venue> getVenue(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getVenue(id));
    }

    @PutMapping("/venues/{id}")
    public ResponseEntity<Venue> updateVenue(
            @PathVariable Long id,
            @RequestBody Venue venue) {

        return ResponseEntity.ok(
                eventService.updateVenue(id, venue));
    }

    @DeleteMapping("/venues/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        eventService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- EVENTS ----------------

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody Event event) {

        return ResponseEntity.ok(
                eventService.updateEvent(id, event));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- TICKET TYPES ----------------

    @PostMapping("/events/{eventId}/ticket-types")
    public ResponseEntity<TicketType> createTicketType(
            @PathVariable Long eventId,
            @RequestBody TicketType ticketType) {

        return ResponseEntity.ok(
                eventService.createTicketType(eventId, ticketType));
    }

    @GetMapping("/events/{eventId}/ticket-types")
    public ResponseEntity<List<TicketType>> getTicketTypes(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                eventService.getTicketTypes(eventId));
    }

    @GetMapping("/ticket-types/{id}")
    public ResponseEntity<TicketType> getTicketType(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                eventService.getTicketType(id));
    }

    @PutMapping("/ticket-types/{id}")
    public ResponseEntity<TicketType> updateTicketType(
            @PathVariable Long id,
            @RequestBody TicketType ticketType) {

        return ResponseEntity.ok(
                eventService.updateTicketType(id, ticketType));
    }

    @DeleteMapping("/ticket-types/{id}")
    public ResponseEntity<Void> deleteTicketType(
            @PathVariable Long id) {

        eventService.deleteTicketType(id);
        return ResponseEntity.noContent().build();
    }
}