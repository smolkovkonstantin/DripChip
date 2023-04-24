package com.example.dripchip.repositorie;

import com.example.dripchip.entites.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationDAO extends JpaRepository<LocationPoint, Long> {
    Optional<LocationPoint> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
