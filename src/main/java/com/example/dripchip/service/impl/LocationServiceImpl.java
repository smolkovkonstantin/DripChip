package com.example.dripchip.service.impl;

import com.example.dripchip.dto.LocationDTO;
import com.example.dripchip.entites.Location;
import com.example.dripchip.repositories.LocationDAO;
import com.example.dripchip.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationDAO locationDAO;

    public ResponseEntity<LocationDTO.Response.Location> addLocation(LocationDTO.Request.Location location) {

        var locationByLatitude = locationDAO.findByLatitude(location.getLatitude());

        var locationByLongitude = locationDAO.findByLongitude(location.getLongitude());

        if (isConflict(locationByLatitude, locationByLongitude)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Location newLocation = Location
                .builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        locationDAO.save(newLocation);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LocationDTO.Response.Location
                        .builder()
                        .id(newLocation.getId())
                        .latitude(newLocation.getLatitude())
                        .longitude(newLocation.getLongitude())
                        .build());
    }

    @Override
    public ResponseEntity<LocationDTO.Response.Location> findLocationById(Long pointId) {

        var opLocation = locationDAO.findById(pointId);

        return opLocation.map(location -> ResponseEntity.ok().body(LocationDTO.Response.Location
                .builder()
                .id(location.getId())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<LocationDTO.Response.Location> updateById(Long pointId, LocationDTO.Request.Location location) {

        var locationById = locationDAO.findById(pointId);

        if (locationById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var locationByLatitude = locationDAO.findByLatitude(location.getLatitude());
        var locationByLongitude = locationDAO.findByLongitude(location.getLongitude());

        if (isConflict(locationByLatitude, locationByLongitude)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        locationDAO.updateLatitudeAndLongitudeById(location.getLatitude(), location.getLongitude(), pointId);
        return ResponseEntity.ok().body(LocationDTO.Response.Location
                .builder()
                .id(pointId)
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build());
    }

    @Override
    public ResponseEntity<LocationDTO.Response.Empty> deleteById(Long pointId) {

        var opLocation = locationDAO.findById(pointId);

        if (opLocation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        locationDAO.deleteById(pointId);

        return ResponseEntity.ok().body(new LocationDTO.Response.Empty());
    }

    private boolean isConflict(Optional<Location> locationByLatitude, Optional<Location> locationByLongitude) {
        return locationByLatitude.isPresent() && locationByLongitude.isPresent() &&
                locationByLatitude.get().equals(locationByLongitude.get());
    }

}
