package com.dinidu.event_booking_service.controller;

import com.dinidu.event_booking_service.entity.*;
import com.dinidu.event_booking_service.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;

    public EventController(VenueRepository venueRepository, EventRepository eventRepository,
            TicketTypeRepository ticketTypeRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @PostMapping("/venues")
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        return ResponseEntity.ok(venueRepository.save(venue));
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventRepository.save(event));
    }

    @PostMapping("/{eventId}/ticket-types")
    public ResponseEntity<TicketType> createTicketType(@PathVariable Long eventId, @RequestBody TicketType ticketType) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        ticketType.setEvent(event);
        ticketType.setSeatsRemaining(ticketType.getSeatsTotal());
        return ResponseEntity.ok(ticketTypeRepository.save(ticketType));
    }
}