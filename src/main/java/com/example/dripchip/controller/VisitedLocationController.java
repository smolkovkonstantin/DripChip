package com.example.dripchip.controller;

import com.example.dripchip.dto.VisitedLocationDTO;
import com.example.dripchip.dto.VisitedLocationDTO.Request;
import com.example.dripchip.dto.VisitedLocationDTO.Response;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.VisitedLocationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals/{animalId}/locations")
public class VisitedLocationController {
    private final VisitedLocationsService visitedLocationsService;

    @PostMapping("/{pointId}")
    public ResponseEntity<Response.VisitedLocationInfo> addVisitedLocationToAnimal(
            @PathVariable Long animalId,
            @PathVariable Long pointId) throws NotFoundException, BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(visitedLocationsService.addVisitedLocationToAnimal(animalId, pointId));
    }

    @PutMapping
    public ResponseEntity<Response.VisitedLocationInfo> updateVisitedLocationInAnimal(
            @PathVariable Long animalId,
            @RequestBody Request.Update update) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok(visitedLocationsService.updateVisitedLocationInAnimal(animalId, update));
    }

    @DeleteMapping("/{visitedPointId}")
    public void deleteVisitedLocationFromAnimal(
            @PathVariable Long animalId, @PathVariable Long visitedPointId) throws NotFoundException {
        visitedLocationsService.deleteVisitedLocationFromAnimal(animalId, visitedPointId);
    }

    @GetMapping
    public ResponseEntity<List<Response.VisitedLocationInfo>> searchVisitedLocation(
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @PathVariable Long animalId) throws NotFoundException, ParseException, BadRequestException {

        VisitedLocationDTO.Request.Search searchDTO = VisitedLocationDTO.Request.Search
                .builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .from(from)
                .size(size)
                .build();

        return ResponseEntity.ok(visitedLocationsService.search(animalId, searchDTO));
    }
}
