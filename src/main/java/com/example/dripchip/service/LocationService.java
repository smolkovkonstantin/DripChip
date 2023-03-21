package com.example.dripchip.service;

import com.example.dripchip.dto.LocationDTO;
import org.springframework.http.ResponseEntity;

public interface LocationService {
    ResponseEntity<LocationDTO.Response.Location> addLocation(LocationDTO.Request.Location location);

    ResponseEntity<LocationDTO.Response.Location> findLocationById(Long pointId);

    ResponseEntity<LocationDTO.Response.Location> updateById(Long pointId, LocationDTO.Request.Location location);

    ResponseEntity<LocationDTO.Response.Empty> deleteById(Long pointId);
}
