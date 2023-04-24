package com.example.dripchip.service;

import com.example.dripchip.dto.VisitedLocationDTO;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface VisitedLocationsService {
    VisitedLocationDTO.Response.VisitedLocationInfo addVisitedLocationToAnimal(@Min(1) @NotNull Long animalId, @Min(1) @NotNull Long pointId) throws NotFoundException, BadRequestException;

    VisitedLocationDTO.Response.VisitedLocationInfo updateVisitedLocationInAnimal(@Min(1) @NotNull Long animalId, VisitedLocationDTO.Request.Update update) throws NotFoundException, BadRequestException;

    void deleteVisitedLocationFromAnimal(@Min(1) @NotNull Long animalId, @Min(1) @NotNull Long pointId) throws NotFoundException;

    Optional<List<VisitedLocationDTO.Response.VisitedLocationInfo>> search(Long animalId, VisitedLocationDTO.Request.Search searchDTO) throws NotFoundException, ParseException;
}
