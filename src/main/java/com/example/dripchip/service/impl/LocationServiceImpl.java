package com.example.dripchip.service.impl;

import com.example.dripchip.dto.LocationDTO;
import com.example.dripchip.entites.LocationPoint;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositories.LocationDAO;
import com.example.dripchip.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class LocationServiceImpl implements LocationService {

    private final LocationDAO locationDAO;

    @Override
    public Optional<LocationDTO.Response.Location> addLocation(@Valid LocationDTO.Request.Location location)
            throws ConflictException {

        var locationByLatitude = locationDAO.findByLatitude(location.getLatitude());

        var locationByLongitude = locationDAO.findByLongitude(location.getLongitude());

        if (isConflict(locationByLatitude, locationByLongitude)) {
            throw new ConflictException("This location is already exists");
        }

        LocationPoint newLocation = LocationPoint
                .builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        locationDAO.save(newLocation);

        return Optional.of(LocationDTO.Response.Location
                        .builder()
                        .id(newLocation.getId())
                        .latitude(newLocation.getLatitude())
                        .longitude(newLocation.getLongitude())
                        .build());
    }

    @Override
    public Optional<LocationPoint> findLocationById(@Min(1) @NotNull Long pointId) {
        return locationDAO.findById(pointId);
    }

    @Override
    public Optional<LocationDTO.Response.Location> updateById(@Min(1) @NotNull  Long pointId, @Valid LocationDTO.Request.Location location) throws ConflictException {

        var locationById = locationDAO.findById(pointId);

        if (locationById.isEmpty()) {
            return Optional.empty();
        }

        var locationByLatitude = locationDAO.findByLatitude(location.getLatitude());
        var locationByLongitude = locationDAO.findByLongitude(location.getLongitude());

        if (isConflict(locationByLatitude, locationByLongitude)) {
            throw new ConflictException("This location is already exists");
        }

        locationDAO.updateLatitudeAndLongitudeById(location.getLatitude(), location.getLongitude(), pointId);
        return Optional.of(LocationDTO.Response.Location
                .builder()
                .id(pointId)
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build());
    }

    @Override
    public void deleteById(@Min(1) @NotNull  Long pointId) throws NotFoundException {

        var opLocation = locationDAO.findById(pointId);

        if (opLocation.isEmpty()) {
            throw new NotFoundException("LocationPoint not found");
        }

        locationDAO.deleteById(pointId);
    }

    // TODO add validation
    private boolean isConflict(Optional<LocationPoint> locationByLatitude, Optional<LocationPoint> locationByLongitude) {
        return locationByLatitude.isPresent() && locationByLongitude.isPresent() &&
                locationByLatitude.get().equals(locationByLongitude.get());
    }

}
