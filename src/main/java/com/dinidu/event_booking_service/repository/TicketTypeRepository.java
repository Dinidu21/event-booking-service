package com.dinidu.event_booking_service.repository;

import com.dinidu.event_booking_service.entity.TicketType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    // Pessimistic write lock: this row is locked until the enclosing @Transactional
    // commits or rolls back. A second concurrent booking request for the SAME
    // ticket type blocks here (in the DB) until the first one finishes — this is
    // the actual mechanism that prevents overselling, not application-level checks.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TicketType t WHERE t.id = :id")
    Optional<TicketType> findByIdForUpdate(Long id);
}