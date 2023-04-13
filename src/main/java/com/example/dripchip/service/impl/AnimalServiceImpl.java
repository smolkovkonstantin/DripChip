package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AnimalsDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.entites.VisitedLocation;
import com.example.dripchip.entites.model.LifeStatus;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositories.AnimalDAO;
import com.example.dripchip.repositories.VisitedLocationsDAO;
import com.example.dripchip.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalDAO animalsDAO;
    private final AnimalsTypesService animalsTypesService;
    private final AccountService accountService;
    private final LocationService locationService;
    private final VisitedLocationService visitedLocationService;
    private final VisitedLocationsDAO visitedLocationsDAO;

    @Override
    public Animal registerAnimal(@Valid AnimalsDTO.Request.Registration registration) throws ConflictException, NotFoundException {

        var opAccount = accountService.getAccountById(registration.getChipperId());
        var opLocation = locationService.findLocationById(registration.getChippingLocationId());

        if (Arrays.stream(registration.getAnimalTypes()).anyMatch(animalsTypesService::isNotExists)
                || opAccount.isEmpty()
                || opLocation.isEmpty()) {
            throw new NotFoundException("Type of animal or account with chippingId or location with chippingLocationId doesn't found");
        }

        if (hasDuplicate(registration.getAnimalTypes())) {
            throw new ConflictException("Duplicate in animal type array");
        }

        List<AnimalType> animalTypes = new ArrayList<>();
        Arrays.stream(registration.getAnimalTypes()).forEach(
                animalsTypeId -> animalTypes.add(animalsTypesService.getEntityAnimalsTypesById(animalsTypeId))
        );

        VisitedLocation visitedLocation = VisitedLocation.builder()
                .locationPoint(opLocation.get())
                .dateTimeOfVisitLocationPoint(new Date())
                .build();

        Animal animal = Animal.builder()
                .animalTypes(animalTypes)
                .animalTypesJson(Arrays.asList(registration.getAnimalTypes()))
                .visitedLocationsJson(new ArrayList<>(List.of(registration.getChippingLocationId())))
                .account(opAccount.get())
                .chipperId(registration.getChipperId())
                .chippingLocationId(registration.getChippingLocationId())
                .weight(registration.getWeight())
                .length(registration.getLength())
                .height(registration.getHeight())
                .gender(registration.getGender())
                .lifeStatus(LifeStatus.ALIVE.name())
                .chippingDateTime(new Date())
                .deathDateTime(null)
                .build();

        animalsDAO.save(animal);
        animalsDAO.flush();

        visitedLocation.setAnimal(new ArrayList<>(List.of(animal)));
        visitedLocationService.save(visitedLocation);

        animal.setVisitedLocations(new ArrayList<>(List.of(visitedLocation)));
        return animalsDAO.save(animal);
    }

    @Override
    public Animal getAnimalById(@Min(1) @NotNull Long animalId) throws NotFoundException {
        var opAnimal = animalsDAO.findById(animalId);
        if (opAnimal.isEmpty()) throw new NotFoundException("Animal not found");
        opAnimal.get().setAnimalTypesJson(opAnimal.get().getAnimalTypes().stream().map(AnimalType::getId).toList());
        opAnimal.get().setVisitedLocationsJson(opAnimal.get().getVisitedLocations().stream().map(VisitedLocation::getId).toList());
        return opAnimal.get();
    }

    @Override
    public Animal update(@NotNull @Min(1) Long animalId, @Valid AnimalsDTO.Request.Update update) throws NotFoundException {

        var opAnimal = animalsDAO.findById(animalId);
        var opAccount = accountService.getAccountById(update.getChipperId());
        var opLocation = locationService.findLocationById(update.getChippingLocationId());

        if (opAccount.isEmpty() || opLocation.isEmpty() || opAnimal.isEmpty()) {
            throw new NotFoundException("Not found animal or location or chipper by id");
        }

        Date deathDateTime = null;

        if (update.getLifeStatus().equals(LifeStatus.DEAD.name())) {
            deathDateTime = new Date();
        }

        var visitedLocations = opAnimal.get().getVisitedLocations();
        if (visitedLocations.get(0).getId().equals(update.getChippingLocationId())) {
            throw new NotFoundException("Not found animal or location or chipper by id");
        }

        VisitedLocation visitedLocation = VisitedLocation.builder()
                .locationPoint(opLocation.get())
                .dateTimeOfVisitLocationPoint(new Date())
                .build();

        visitedLocationsDAO.save(visitedLocation);

        visitedLocations.add(visitedLocation);

        animalsDAO.updateAnimal(
                update.getWeight(), update.getLength(), update.getHeight(), update.getGender(), update.getLifeStatus(), deathDateTime,
                visitedLocations, opAccount.get(), animalId
        );
        return animalsDAO.findById(animalId).get();
    }

    @Override
    public void deleteAnimalById(@NotNull @Min(0) Long animalId) throws NotFoundException, BadRequestException {
        var opAnimal = animalsDAO.findById(animalId);
        if (opAnimal.isEmpty()) {
            throw new NotFoundException("Animal not found");
        }

        if (hasVisitedLocation(opAnimal.get())) {
            throw new BadRequestException("Animal has visited locations");
        }

        animalsDAO.delete(opAnimal.get());
    }

    private boolean hasVisitedLocation(Animal animal) {
        return animal.getVisitedLocations().size() > 1;
    }


    private boolean hasDuplicate(Long[] array) {
        return Arrays.stream(array).distinct().count() < array.length;
    }
}
