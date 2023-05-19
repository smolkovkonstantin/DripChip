package com.example.dripchip.repositorie;

import com.example.dripchip.entites.VisitedLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitedLocationsDAO extends JpaRepository<VisitedLocation, Long> {
    List<VisitedLocation> findByAnimalId(Long id);
}
