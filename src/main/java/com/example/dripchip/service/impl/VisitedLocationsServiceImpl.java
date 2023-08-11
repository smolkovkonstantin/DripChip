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
import jakarta.validation.Valid;
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

        var locationPoint = locationService.findLocationById(pointId);
        var visitedLocations = animal.getVisitedLocations();

        if (animal.getLifeStatus().equals(LifeStatus.DEAD.name()))
            throw new BadRequestException("Animal has already dead");

        if (animal.getChippingLocation().getId().equals(pointId) && visitedLocations.isEmpty())
            throw new BadRequestException("An attempt to add first visit location point equal to the chipping point");

        int lastIndex = visitedLocations.size() - 1;

        if (!visitedLocations.isEmpty()
                && visitedLocations.get(lastIndex).getLocationPoint().equals(locationPoint)) {
            throw new BadRequestException("An attempt to add a visit location where an animal is already located");
        }

        VisitedLocation visitedLocation = VisitedLocation.builder()
                .animal(animal)
                .locationPoint(locationPoint)
                .dateTimeOfVisitLocationPoint(new Date())
                .build();

        visitedLocationsDAO.save(visitedLocation);

        animal.getVisitedLocations().add(visitedLocation);
        animalService.saveAnimal(animal);

        return parseToDTO(visitedLocation);
    }

    @Override
    public Response.VisitedLocationInfo updateVisitedLocationInAnimal(
            @Min(1) @NotNull Long animalId, @Valid Request.Update update) throws NotFoundException, BadRequestException {
        Animal animal = animalService.findById(animalId);
        List<VisitedLocation> visitedLocations = animal.getVisitedLocations();
        VisitedLocation visitedLocation = getVisitedLocationById(update.getVisitedLocationPointId());
        LocationPoint locationPoint = locationService.findLocationById(update.getLocationPointId());

        if (visitedLocations.isEmpty() || !visitedLocations.contains(visitedLocation)) {
            throw new NotFoundException("Animal didn't visit this location");
        }

        int index = visitedLocations.indexOf(visitedLocation);

        if (index == 0 && locationPoint.getId().equals(animal.getChippingLocation().getId())) {
            throw new BadRequestException("Attempt update first visited location to chipping location");
        }

        if (visitedLocation.getLocationPoint().equals(locationPoint)) {
            throw new BadRequestException("Attempt to update a visited location that already exists");
        }

        if (isNeighbor(index, visitedLocations, locationPoint)) {
            throw new BadRequestException("Attempt to update a visited location that matcher with the next or previous");
        }


        visitedLocation.setLocationPoint(locationPoint);
        visitedLocationsDAO.save(visitedLocation);

        visitedLocations.get(index).setLocationPoint(locationPoint);
        animal.setVisitedLocations(visitedLocations);
        animalService.saveAnimal(animal);

        return parseToDTO(visitedLocation);
    }

    private boolean isNeighbor(int index, List<VisitedLocation> visitedLocations, LocationPoint locationPoint) {
        if (0 < index && index < visitedLocations.size() - 1) {
            return (visitedLocations.get(index - 1).getLocationPoint().equals(locationPoint) || visitedLocations.get(index + 1).getLocationPoint().equals(locationPoint));
        } else if (index > 0) {
            return (visitedLocations.get(index - 1).getLocationPoint().equals(locationPoint));
        } else if (index < visitedLocations.size() - 1) {
            return (visitedLocations.get(index + 1).getLocationPoint().equals(locationPoint));
        }
        return false;
    }

    @Override
    public void deleteVisitedLocationFromAnimal(
            @Min(1) @NotNull Long animalId,
            @Min(1) @NotNull Long pointId) throws NotFoundException, BadRequestException {
        Animal animal = animalService.findById(animalId);
        VisitedLocation visitedLocation = getVisitedLocationById(pointId);

        int index = animal.getVisitedLocations().indexOf(visitedLocation);

        if (index != -1) {

            if (index == 0 && animal.getVisitedLocations().size() > 1
                    && animal.getVisitedLocations().get(index + 1).getLocationPoint().getId()
                    .equals(animal.getChippingLocation().getId())) {
                var visitedLocationEqualsChippingLocation = animal.getVisitedLocations().get(index + 1);
                animal.getVisitedLocations().remove(index + 1);
                animalService.saveAnimal(animal);
                visitedLocationsDAO.delete(visitedLocationEqualsChippingLocation);
            }

            animal.getVisitedLocations().remove(index);
            animalService.saveAnimal(animal);
            visitedLocationsDAO.delete(visitedLocation);
        } else {
            throw new NotFoundException("Animal hasn't visited this location");
        }
    }

    @Override
    public List<Response.VisitedLocationInfo> search
            (Long animalId, Request.Search searchDTO) throws NotFoundException, ParseException, BadRequestException {

        if (searchDTO.getFrom() < 0 || searchDTO.getSize() <= 0) {
            throw new BadRequestException("From must can't be less than 1 and size can't be less and equal than 0");
        }

        Animal animal = animalService.findById(animalId);
        List<VisitedLocation> visitedLocations = animal.getVisitedLocations();

        return searchByParameters(searchDTO.getStartDateTime(), searchDTO.getEndDateTime(),
                searchDTO.getFrom(), searchDTO.getSize(), visitedLocations);
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
            String stringStartDateTime, String stringEndDateTime,
            int from, int size,
            List<VisitedLocation> visitedLocations) throws ParseException {

        DateFormat dateFormat = new StdDateFormat();

        List<Response.VisitedLocationInfo> result = new ArrayList<>();

        Date startDateTime = (stringStartDateTime != null) ? dateFormat.parse(stringStartDateTime) : null;
        Date endDateTime = (stringStartDateTime != null) ? dateFormat.parse(stringEndDateTime) : null;

        visitedLocations.forEach(visitedLocation -> {

            if (stringStartDateTime == null && stringEndDateTime == null) {
                result.add(parseToDTO(visitedLocation));
            } else if (stringStartDateTime != null && visitedLocation.getDateTimeOfVisitLocationPoint().before(startDateTime)) {
                result.add(parseToDTO(visitedLocation));
            } else if (stringEndDateTime != null && visitedLocation.getDateTimeOfVisitLocationPoint().after(endDateTime)) {
                result.add(parseToDTO(visitedLocation));
            } else if (visitedLocation.getDateTimeOfVisitLocationPoint().after(startDateTime)
                    && visitedLocation.getDateTimeOfVisitLocationPoint().before(endDateTime)) {
                result.add(parseToDTO(visitedLocation));
            }
        });

        return result.stream().skip(from).limit(size).toList();
    }
}
