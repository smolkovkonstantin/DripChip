package com.example.dripchip.repositories;

import com.example.dripchip.entites.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LocationDAO extends JpaRepository<Location, Long> {
    @Transactional
    @Modifying
    @Query("update Location l set l.latitude = ?1, l.longitude = ?2 where l.id = ?3")
    void updateLatitudeAndLongitudeById(Double latitude, Double longitude, Long id);
    Optional<Location> findByLongitude(Double longitude);
    Optional<Location> findByLatitude(Double latitude);

    @Override
    Optional<Location> findById(Long id);
}
