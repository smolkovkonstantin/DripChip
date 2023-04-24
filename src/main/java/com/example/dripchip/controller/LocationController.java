package com.example.dripchip.controller;

import com.example.dripchip.dto.LocationDTO.Request;
import com.example.dripchip.dto.LocationDTO.Response;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Response.Location> addLocation(
            @RequestBody Request.Location location) throws ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.addLocation(location));
    }

    @GetMapping("/{pointId}")
    public ResponseEntity<Response.Location> findLocationById(
            @PathVariable Long pointId) throws NotFoundException {
        return ResponseEntity.ok(locationService.parseToDTO(locationService.findLocationById(pointId)));
    }


    @PutMapping("/{pointId}")
    public ResponseEntity<Response.Location> updateById(
            @PathVariable Long pointId,
            @RequestBody Request.Location locationDTO) throws ConflictException, NotFoundException {
        return ResponseEntity.ok(locationService.updateById(pointId, locationDTO));
    }

    @DeleteMapping("/{pointId}")
    public void deleteLocationById(@PathVariable Long pointId) throws NotFoundException {
        locationService.deleteById(pointId);
    }

}
