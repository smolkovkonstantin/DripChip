package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AnimalDTO.Response;
import com.example.dripchip.dto.AnimalDTO.Request;
import com.example.dripchip.entites.*;
import com.example.dripchip.entites.model.LifeStatus;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositorie.AnimalDAO;
import com.example.dripchip.repositorie.AnimalTypeAnimalDAO;
import com.example.dripchip.service.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalDAO animalDAO;
    private final TypesService typesService;
    private final AccountService accountService;
    private final LocationService locationService;
    private final AnimalTypeAnimalDAO animalTypeAnimalDAO;

    @Override
    public Response.Information registration(@Valid Request.Registration registration) throws ConflictException, NotFoundException {

        Account account = accountService.getAccountById(registration.getChipperId()).orElseThrow(() -> new NotFoundException("Account doesn't exists"));
        LocationPoint locationPoint = locationService.findLocationById(registration.getChippingLocationId());

        List<AnimalType> animalTypes = new ArrayList<>();
        registration.getAnimalTypes().forEach(typeId -> {
            try {
                animalTypes.add(typesService.getEntityAnimalsTypesById(typeId));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        hasDuplicate(registration.getAnimalTypes());

        Animal animal = Animal.builder()
                .account(account)
                .weight(registration.getWeight())
                .length(registration.getLength())
                .height(registration.getHeight())
                .gender(registration.getGender())
                .lifeStatus(LifeStatus.ALIVE.name())
                .chippingDateTime(new Date())
                .deathDateTime(null)
                .chippingLocation(locationPoint)
                .build();

        saveAnimal(animal);

        List<AnimalTypeAnimal> animalTypeAnimals = new ArrayList<>();

        animalTypes.forEach(animalType -> animalTypeAnimals.add(AnimalTypeAnimal
                .builder()
                .animal(animal)
                .animalType(animalType)
                .build())
        );

        animalTypeAnimalDAO.saveAll(animalTypeAnimals);

        animal.setAnimalTypeAnimals(animalTypeAnimals);

        return parseToDTO(animal);
    }

    @Override
    public List<Response.Information> search(Request.Search searchDTO) throws ParseException, BadRequestException {


        if (searchDTO.getFrom() < 0 || searchDTO.getSize() <= 0) {
            throw new BadRequestException("From must can't be less than 1 and size can't be less and equal than 0");
        }

        DateFormat dateFormat = new StdDateFormat();

        List<Animal> animals = animalDAO.searchByParameters(
                searchDTO.getChipperId(), searchDTO.getChippingLocationId(),
                searchDTO.getLifeStatus(), searchDTO.getGender(),
                searchDTO.getStartDateTime() == null ? null : dateFormat.parse(searchDTO.getStartDateTime()),
                searchDTO.getEndDateTime() == null ? null : dateFormat.parse(searchDTO.getEndDateTime())
        );

        return animals.stream().map(this::parseToDTO).skip(searchDTO.getFrom()).limit(searchDTO.getSize()).toList();
    }

    @Override
    public Response.Information getById(@Min(1) @NotNull Long animalId) throws NotFoundException {
        Animal animal = animalDAO.findById(animalId).orElseThrow(() -> new NotFoundException("Animal not found"));

        return parseToDTO(animal);
    }

    @Override
    public Response.Information update(@NotNull @Min(1) Long animalId, @Valid Request.Update update) throws NotFoundException, BadRequestException {

        Animal animal = animalDAO.findById(animalId).orElseThrow(() -> new NotFoundException("Not found animal by id"));
        Account account = accountService.getAccountById(update.getChipperId()).orElseThrow(() -> new NotFoundException("Account doesn't exists"));
        LocationPoint locationPoint = locationService.findLocationById(update.getChippingLocationId());

        Date deathDateTime = update.getLifeStatus().equals(LifeStatus.DEAD.name()) ? new Date() : null;

        if (animal.getLifeStatus().equals(LifeStatus.DEAD.name()) && update.getLifeStatus().equals(LifeStatus.ALIVE.name())) {
            throw new BadRequestException("Attention set alive life status to dead animal");
        }

        if (!animal.getVisitedLocations().isEmpty()) {
            if (animal.getVisitedLocations().get(0).getLocationPoint().getId().equals(update.getChippingLocationId())) {
                throw new BadRequestException("The new chip point coincides with the first visited location point");
            }

            if (animal.getVisitedLocations().size() == 1 && locationPoint.equals(animal.getChippingLocation())) {
                throw new BadRequestException("");
            }
        }

        animalDAO.updateAnimal(update.getWeight(), update.getLength(), update.getHeight(), update.getGender(),
                update.getLifeStatus(), deathDateTime, account, locationPoint, animalId);

        animal.setWeight(update.getWeight());
        animal.setHeight(update.getHeight());
        animal.setLength(update.getLength());
        animal.setGender(update.getGender());
        animal.setLifeStatus(update.getLifeStatus());
        animal.setAccount(account);
        animal.setChippingLocation(locationPoint);
        animal.setDeathDateTime(deathDateTime);

        animalDAO.save(animal);

        return parseToDTO(animal);
    }

    @Override
    public void deleteById(@NotNull @Min(1) Long animalId) throws NotFoundException, BadRequestException {
        Animal animal = animalDAO.findById(animalId).orElseThrow(() -> new NotFoundException("Animal not found"));

        if (!animal.getVisitedLocations().isEmpty()) {
            throw new BadRequestException("Animal has visited locations");
        }

        animalTypeAnimalDAO.deleteAll(animalTypeAnimalDAO.findByAnimal_Id(animalId));

        animalDAO.delete(animal);
    }

    @Override
    public Response.Information parseToDTO(Animal animal) {
        return Response.Information.builder()
                .id(animal.getId())
                .animalTypes(Optional.ofNullable(animal.getAnimalTypeAnimals())
                        .orElse(new ArrayList<>()).stream().map(animalTypeAnimal -> animalTypeAnimal.getAnimalType().getId()).toList())
                .weight(animal.getWeight())
                .length(animal.getLength())
                .height(animal.getHeight())
                .gender(animal.getGender())
                .lifeStatus(animal.getLifeStatus())
                .chippingDateTime(animal.getChippingDateTime())
                .chipperId(animal.getAccount().getId())
                .chippingLocationId(animal.getChippingLocation().getId())
                .visitedLocations(
                        Optional.ofNullable(animal.getVisitedLocations())
                                .orElse(new ArrayList<>()).stream()
                                .map(VisitedLocation::getId)
                                .toList())
                .deathDateTime(animal.getDeathDateTime())
                .build();
    }

    @Override
    public Animal findById(@NotNull @Min(1) Long animalId) throws NotFoundException {
        return animalDAO.findById(animalId).orElseThrow(() -> new NotFoundException("Not found animal by id"));
    }

    @Override
    public void saveAnimal(Animal animal) {
        animalDAO.save(animal);
    }

    private void hasDuplicate(List<Long> array) throws ConflictException {
        if (array.stream().distinct().count() < array.size()) {
            throw new ConflictException("Duplicate in animal type array" + array);
        }
    }
}
