package com.example.dripchip.service;

import com.example.dripchip.dto.LocationDTO;
import com.example.dripchip.entites.LocationPoint;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface LocationService {
    Optional<LocationDTO.Response.Location> addLocation(@Valid LocationDTO.Request.Location location) throws ConflictException;

    Optional<LocationPoint> findLocationById(@Min(1) @NotNull  Long pointId);

    Optional<LocationDTO.Response.Location> updateById(@Min(1) @NotNull  Long pointId, @Valid LocationDTO.Request.Location location) throws ConflictException;

    void deleteById(@Min(1) @NotNull  Long pointId) throws NotFoundException;
}
