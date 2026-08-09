package com.dinidu.event_booking_service.repository;

import com.dinidu.event_booking_service.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByOrganizerId(Long organizerId);

	// Find events by the associated venue id
	List<Event> findByVenue_Id(Long venueId);

	// Find events starting between two instants
	List<Event> findByStartsAtBetween(Instant start, Instant end);
}
