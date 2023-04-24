package com.example.dripchip.service.impl;

import com.example.dripchip.dto.VisitedLocationDTO.Response;
import com.example.dripchip.dto.VisitedLocationDTO.Request;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.LocationPoint;
import com.example.dripchip.entites.VisitedLocation;
import com.example.dripchip.entites.model.LifeStatus;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositorie.VisitedLocationsDAO;
import com.example.dripchip.service.AnimalService;
import com.example.dripchip.service.LocationService;
import com.example.dripchip.service.VisitedLocationsService;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class VisitedLocationsServiceImpl implements VisitedLocationsService {

    private final VisitedLocationsDAO visitedLocationsDAO;
    private final AnimalService animalService;
    private final LocationService locationService;

    @Override
    public Response.VisitedLocationInfo addVisitedLocationToAnimal(
            @Min(1) @NotNull Long animalId,
            @Min(1) @NotNull Long pointId) throws NotFoundException, BadRequestException {
        Animal animal = animalService.findById(animalId);

        if (animal.getLifeStatus().equals(LifeStatus.DEAD.name()))
            throw new BadRequestException("Animal has already dead");

        if (animal.getChippingLocationId().equals(pointId))
            throw new BadRequestException("An attempt to add a visit location point equal to the chipping point");


        VisitedLocation visitedLocation = VisitedLocation.builder()
                .locationPoint(locationService.findLocationById(pointId))
                .dateTimeOfVisitLocationPoint(new Date())
                .build();

        int lastIndex = animal.getVisitedLocations().size() - 1;

        if (animal.getVisitedLocations().size() != 0 && animal.getVisitedLocations().get(lastIndex).getLocationPoint().getId().equals(pointId)) {
            throw new BadRequestException("An attempt to add a visit location where an animal is already located");
        } else {
            visitedLocationsDAO.save(visitedLocation);
            animal.getVisitedLocations().add(visitedLocation);
            animalService.saveAnimal(animal);
        }

        return parseToDTO(visitedLocation);
    }

    @Override
    public Response.VisitedLocationInfo updateVisitedLocationInAnimal(
            @Min(1) @NotNull Long animalId, Request.Update update) throws NotFoundException, BadRequestException {
        Animal animal = animalService.findById(animalId);
        List<VisitedLocation> visitedLocations = animal.getVisitedLocations();
        VisitedLocation visitedLocation = getVisitedLocationById(update.getVisitedLocationPointId());
        LocationPoint locationPoint = locationService.findLocationById(update.getLocationPointId());

        if (visitedLocations == null || visitedLocations.size() == 0 || !visitedLocations.contains(visitedLocation)) {
            throw new NotFoundException("Animal didn't visit this location");
        }

        int index = visitedLocations.indexOf(visitedLocation);

        if (index == 0 && visitedLocation.getLocationPoint().getId().equals(animal.getChippingLocationId())) {
            throw new BadRequestException("Attempt update first visited location to chipping location");
        }

        if (visitedLocation.getLocationPoint().equals(locationPoint)) {
            throw new BadRequestException("Attempt to update a visited location that already exists");
        }

        visitedLocation.setLocationPoint(locationPoint);
        visitedLocationsDAO.save(visitedLocation);

        visitedLocations.get(index).setLocationPoint(locationPoint);
        animal.setVisitedLocations(visitedLocations);
        animalService.saveAnimal(animal);

        return parseToDTO(visitedLocation);
    }

    @Override
    public void deleteVisitedLocationFromAnimal(
            @Min(1) @NotNull Long animalId,
            @Min(1) @NotNull Long pointId) throws NotFoundException {
        Animal animal = animalService.findById(animalId);
        VisitedLocation visitedLocation = getVisitedLocationById(pointId);

        int id = animal.getVisitedLocations().indexOf(visitedLocation);

        if (id != -1) {
            animal.getVisitedLocations().remove(id);
            visitedLocationsDAO.delete(visitedLocation);
            animalService.saveAnimal(animal);
        } else {
            throw new NotFoundException("Animal hasn't visited this location");
        }
    }

    @Override
    public Optional<List<Response.VisitedLocationInfo>> search
            (Long animalId, Request.Search searchDTO) throws NotFoundException, ParseException {
        Animal animal = animalService.findById(animalId);
        List<VisitedLocation> visitedLocations = animal.getVisitedLocations();

        DateFormat dateFormat = new StdDateFormat();

        return Optional.of(searchByParameters(dateFormat.parse(searchDTO.getStartDateTime()), dateFormat.parse(searchDTO.getEndDateTime()),
                searchDTO.getFrom(), searchDTO.getSize(), visitedLocations));
    }

    private Response.VisitedLocationInfo parseToDTO(VisitedLocation visitedLocation) {
        return Response.VisitedLocationInfo.builder()
                .id(visitedLocation.getId())
                .dateTimeOfVisitLocationPoint(visitedLocation.getDateTimeOfVisitLocationPoint())
                .locationPointId(visitedLocation.getLocationPoint().getId())
                .build();
    }

    private VisitedLocation getVisitedLocationById(Long pointId) throws NotFoundException {
        return visitedLocationsDAO.findById(pointId)
                .orElseThrow(() -> new NotFoundException("Object with information about visit location not found"));
    }

    private List<Response.VisitedLocationInfo> searchByParameters(
            Date startDateTime, Date endDateTime,
            int from, int size,
            List<VisitedLocation> visitedLocations) {

        List<Response.VisitedLocationInfo> result = new ArrayList<>();

        visitedLocations.forEach(visitedLocation -> {

            if (startDateTime == null && endDateTime == null) {
                result.add(parseToDTO(visitedLocation));
            } else if (startDateTime == null && visitedLocation.getDateTimeOfVisitLocationPoint().before(endDateTime)) {
                result.add(parseToDTO(visitedLocation));
            } else if (endDateTime == null && visitedLocation.getDateTimeOfVisitLocationPoint().after(startDateTime)) {
                result.add(parseToDTO(visitedLocation));
            } else if (visitedLocation.getDateTimeOfVisitLocationPoint().after(startDateTime)
                    && visitedLocation.getDateTimeOfVisitLocationPoint().before(endDateTime)) {
                result.add(parseToDTO(visitedLocation));
            }
        });

        return result.stream().limit(size).skip(from).toList();
    }
}