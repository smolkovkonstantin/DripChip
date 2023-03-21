package com.example.dripchip.controllers;

import com.example.dripchip.dto.LocationDTO;
import com.example.dripchip.service.LocationService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationDTO.Response.Location> addLocation(@RequestBody LocationDTO.Request.Location location) {
        return locationService.addLocation(location);
    }

    @GetMapping("/{pointId}")
    public ResponseEntity<LocationDTO.Response.Location> findLocationById(@PathVariable @Min(1) @NotNull Long pointId) {
        return locationService.findLocationById(pointId);
    }


    @PutMapping("/{pointId}")
    public ResponseEntity<LocationDTO.Response.Location> updateById(
            @PathVariable Long pointId,
            @RequestBody LocationDTO.Request.Location location) {
        return locationService.updateById(pointId, location);
    }

    @DeleteMapping("/{pointId}")
    public ResponseEntity<LocationDTO.Response.Empty> deleteLocationById(@PathVariable @Min(1) @NotNull Long pointId) {
        return locationService.deleteById(pointId);
    }

}
