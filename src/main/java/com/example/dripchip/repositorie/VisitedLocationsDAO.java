package com.example.dripchip.repositorie;

import com.example.dripchip.entites.VisitedLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Optional;

public interface VisitedLocationsDAO extends JpaRepository<VisitedLocation, Long> {
}
