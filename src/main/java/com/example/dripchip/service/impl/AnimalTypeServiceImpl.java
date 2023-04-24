package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.AnimalService;
import com.example.dripchip.service.TypesService;
import com.example.dripchip.service.AnimalTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalService animalService;
    private final TypesService typesService;

    @Override
    public AnimalDTO.Response.Information addAnimalTypeToAnimal(@Min(0) @NotNull Long animalId, @Min(0) @NotNull Long typeId) throws NotFoundException, ConflictException {

        Animal animal = animalService.findById(animalId);
        AnimalType animalType = typesService.getEntityAnimalsTypesById(typeId);

        if (animal.getAnimalTypes().contains(animalType)) {
            throw new ConflictException("Animal already has this type");
        }

        animal.getAnimalTypes().add(animalType);

        animalService.saveAnimal(animal);

        return animalService.parseToDTO(animal);
    }

    @Override
    public AnimalDTO.Response.Information updateAnimalTypeInAnimal(@Min(1) @NotNull Long animalId, @Valid AnimalDTO.Request.UpdateAnimalType update) throws NotFoundException, ConflictException {
        Animal animal = animalService.findById(animalId);
        AnimalType newAnimalType = typesService.getEntityAnimalsTypesById(update.getNewTypeId());
        AnimalType oldAnimalType = typesService.getEntityAnimalsTypesById(update.getOldTypeId());

        if (!animal.getAnimalTypes().contains(oldAnimalType))
            throw new NotFoundException("Not found old animal type by id");

        if (animal.getAnimalTypes().contains(newAnimalType)) {
            if (animal.getAnimalTypes().contains(oldAnimalType)) {
                throw new ConflictException("Animal with animal id already has type of animal with old id and new id");
            }
            throw new ConflictException("Type of animal with animal type id already has in animal with animal id");
        }

        animal.getAnimalTypes().replaceAll(animalType -> animalType.equals(oldAnimalType) ? newAnimalType : animalType);

        animalService.saveAnimal(animal);

        return animalService.parseToDTO(animal);
    }

    @Override
    public AnimalDTO.Response.Information deleteAnimalTypeFromAnimal(@Min(1) @NotNull Long animalId, @Min(1) @NotNull Long typeId) throws NotFoundException, BadRequestException {
        Animal animal = animalService.findById(animalId);
        AnimalType animalType = typesService.getEntityAnimalsTypesById(typeId);

        if (!animal.getAnimalTypes().contains(animalType)) {
            throw new NotFoundException("Animal with id" + animalId + "hasn't type of animal with id " + typeId);
        } else if (animal.getAnimalTypes().size() == 1) {
            throw new BadRequestException("Animal with id" + animalId + "has only one type of animal with id " + typeId);
        }

        animal.getAnimalTypes().remove(animalType);

        animalService.saveAnimal(animal);

        return animalService.parseToDTO(animal);
    }
}
