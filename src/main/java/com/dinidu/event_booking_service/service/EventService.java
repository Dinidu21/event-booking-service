package com.dinidu.event_booking_service.service;

import com.dinidu.event_booking_service.entity.*;
import com.dinidu.event_booking_service.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final BookingRepository bookingRepository;

    public EventService(
            VenueRepository venueRepository,
            EventRepository eventRepository,
            TicketTypeRepository ticketTypeRepository,
            BookingRepository bookingRepository) {

        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.bookingRepository = bookingRepository;
    }

    // ---------------- VENUE CRUD ----------------

    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenue(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));
    }

    public Venue updateVenue(Long id, Venue request) {
        Venue venue = getVenue(id);

        venue.setName(request.getName());
        venue.setCity(request.getCity());
        venue.setCapacity(request.getCapacity());

        return venueRepository.save(venue);
    }

    public void deleteVenue(Long id) {
        Venue venue = getVenue(id);
        venueRepository.delete(venue);
    }

    // ---------------- EVENT CRUD ----------------

    public Event createEvent(Event event) {
        if (event.getVenue() == null || event.getVenue().getId() == null) {
            throw new IllegalArgumentException("Venue id is required");
        }

        Venue venue = getVenue(event.getVenue().getId());
        event.setVenue(venue);

        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    public Event updateEvent(Long id, Event request) {
        Event event = getEvent(id);

        if (request.getVenue() != null && request.getVenue().getId() != null) {
            event.setVenue(getVenue(request.getVenue().getId()));
        }

        event.setOrganizerId(request.getOrganizerId());
        event.setTitle(request.getTitle());
        event.setStartsAt(request.getStartsAt());
        event.setBannerUrl(request.getBannerUrl());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = getEvent(id);
        eventRepository.delete(event);
    }

    // ---------------- TICKET TYPE CRUD ----------------

    public TicketType createTicketType(Long eventId, TicketType ticketType) {
        Event event = getEvent(eventId);

        ticketType.setEvent(event);
        ticketType.setSeatsRemaining(ticketType.getSeatsTotal());

        return ticketTypeRepository.save(ticketType);
    }

    public List<TicketType> getTicketTypes(Long eventId) {
        getEvent(eventId);

        return ticketTypeRepository.findAll()
                .stream()
                .filter(ticket -> ticket.getEvent().getId().equals(eventId))
                .toList();
    }

    public TicketType getTicketType(Long id) {
        return ticketTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));
    }

    public TicketType updateTicketType(Long id, TicketType request) {
        TicketType ticketType = getTicketType(id);

        ticketType.setName(request.getName());
        ticketType.setPrice(request.getPrice());

        if (request.getSeatsTotal() != null) {
            int soldSeats = ticketType.getSeatsTotal() - ticketType.getSeatsRemaining();

            if (request.getSeatsTotal() < soldSeats) {
                throw new IllegalStateException(
                        "Seats total cannot be lower than already booked seats");
            }

            ticketType.setSeatsTotal(request.getSeatsTotal());
            ticketType.setSeatsRemaining(
                    request.getSeatsTotal() - soldSeats);
        }

        return ticketTypeRepository.save(ticketType);
    }

    public void deleteTicketType(Long id) {
        TicketType ticketType = getTicketType(id);
        ticketTypeRepository.delete(ticketType);
    }

    // ---------------- BOOKING CRUD ----------------

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public Booking updateBooking(Long id, Booking.Status status) {
        Booking booking = getBooking(id);

        if (booking.getStatus() == status) {
            return booking;
        }

        if (status == Booking.Status.CANCELLED) {
            TicketType ticketType = ticketTypeRepository
                    .findByIdForUpdate(booking.getTicketType().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

            ticketType.setSeatsRemaining(
                    ticketType.getSeatsRemaining() + booking.getQuantity());

            booking.setStatus(Booking.Status.CANCELLED);
        }

        if (status == Booking.Status.CONFIRMED) {
            TicketType ticketType = ticketTypeRepository
                    .findByIdForUpdate(booking.getTicketType().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

            if (ticketType.getSeatsRemaining() < booking.getQuantity()) {
                throw new IllegalStateException("Not enough seats remaining");
            }

            ticketType.setSeatsRemaining(
                    ticketType.getSeatsRemaining() - booking.getQuantity());

            booking.setStatus(Booking.Status.CONFIRMED);
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = getBooking(id);

        if (booking.getStatus() == Booking.Status.CONFIRMED) {
            TicketType ticketType = ticketTypeRepository
                    .findByIdForUpdate(booking.getTicketType().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

            ticketType.setSeatsRemaining(
                    ticketType.getSeatsRemaining() + booking.getQuantity());
        }

        bookingRepository.delete(booking);
    }
}