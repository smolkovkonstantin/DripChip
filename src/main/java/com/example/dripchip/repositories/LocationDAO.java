package com.example.dripchip.repositories;

import com.example.dripchip.entites.LocationPoint;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LocationDAO extends JpaRepository<LocationPoint, Long> {
    @Transactional
    @Modifying
    @Query("update LocationPoint l set l.latitude = ?1, l.longitude = ?2 where l.id = ?3")
    void updateLatitudeAndLongitudeById(Double latitude, Double longitude, Long id);
    Optional<LocationPoint> findByLongitude(Double longitude);
    Optional<LocationPoint> findByLatitude(Double latitude);

    @Override
    @NotNull
    Optional<LocationPoint> findById(@NotNull Long id);
}
