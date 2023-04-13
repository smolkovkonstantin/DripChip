package com.example.dripchip.controllers;

import com.example.dripchip.dto.LocationDTO;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<?> addLocation(
            @RequestBody LocationDTO.Request.Location location) throws ConflictException {
        return locationService.addLocation(location).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{pointId}")
    public ResponseEntity<?> findLocationById(
            @PathVariable Long pointId) {
        return locationService.findLocationById(pointId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{pointId}")
    public ResponseEntity<?> updateById(
            @PathVariable Long pointId,
            @RequestBody LocationDTO.Request.Location location) throws ConflictException {
        return locationService.updateById(pointId, location).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{pointId}")
    public void deleteLocationById(@PathVariable Long pointId) throws NotFoundException {
        locationService.deleteById(pointId);
    }

}
