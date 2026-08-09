package com.dinidu.event_booking_service.repository;

import com.dinidu.event_booking_service.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final TicketTypeRepository ticketTypeRepository;
    private final BookingRepository bookingRepository;

    public BookingService(TicketTypeRepository ticketTypeRepository, BookingRepository bookingRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking book(Long userId, Long ticketTypeId, int quantity) {
        // Row lock acquired here — held for the rest of this transaction
        TicketType ticketType = ticketTypeRepository.findByIdForUpdate(ticketTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));

        if (ticketType.getSeatsRemaining() < quantity) {
            throw new IllegalStateException("Not enough seats remaining");
        }

        ticketType.setSeatsRemaining(ticketType.getSeatsRemaining() - quantity);
        // no explicit save() needed for ticketType — it's a managed entity within
        // this @Transactional method, Hibernate flushes the change on commit

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setTicketType(ticketType);
        booking.setQuantity(quantity);

        return bookingRepository.save(booking);
        // lock released here, on transaction commit
    }
}