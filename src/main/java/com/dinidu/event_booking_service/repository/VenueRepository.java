package com.dinidu.event_booking_service.repository;

import com.dinidu.event_booking_service.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

	List<Venue> findByCity(String city);

	List<Venue> findByCapacityGreaterThanEqual(Integer capacity);
}
