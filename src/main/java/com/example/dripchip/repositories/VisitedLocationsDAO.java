package com.example.dripchip.repositories;

import com.example.dripchip.entites.VisitedLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitedLocationsDAO extends JpaRepository<VisitedLocation, Long> {
}
