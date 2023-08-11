package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.entites.AnimalTypeAnimal;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositorie.AnimalTypeAnimalDAO;
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
    private final AnimalTypeAnimalDAO animalTypeAnimalDAO;

    @Override
    public AnimalDTO.Response.Information addAnimalTypeToAnimal(@Min(1) @NotNull Long animalId, @Min(1) @NotNull Long typeId) throws NotFoundException, ConflictException {

        Animal animal = animalService.findById(animalId);
        AnimalType animalType = typesService.getEntityAnimalsTypesById(typeId);

        if (animalTypeAnimalDAO.findByAnimal_IdAndAnimalType_Id(animalId, typeId).isPresent()) {
            throw new ConflictException("Animal already has this type");
        }

        animalTypeAnimalDAO.save(AnimalTypeAnimal.builder()
                .animal(animal)
                .animalType(animalType)
                .build());

        animalService.saveAnimal(animal);

        return animalService.parseToDTO(animal);
    }

    @Override
    public AnimalDTO.Response.Information updateAnimalTypeInAnimal(@Min(1) @NotNull Long animalId, @Valid AnimalDTO.Request.UpdateAnimalType update) throws NotFoundException, ConflictException {
        Animal animal = animalService.findById(animalId);
        AnimalType newAnimalType = typesService.getEntityAnimalsTypesById(update.getNewTypeId());
        AnimalType oldAnimalType = typesService.getEntityAnimalsTypesById(update.getOldTypeId());

        var animalTypeAnimalOld = animalTypeAnimalDAO.findByAnimal_IdAndAnimalType_Id(animalId, oldAnimalType.getId());

        if (animalTypeAnimalOld.isEmpty())
            throw new NotFoundException("Not found old animal type by id");

        if (animalTypeAnimalDAO.findByAnimal_IdAndAnimalType_Id(animalId, newAnimalType.getId()).isPresent()) {
            throw new ConflictException("Animal with animal id already has type of animal with old id and new id");
        }

        animalTypeAnimalOld.get().setAnimalType(newAnimalType);

        animalTypeAnimalDAO.save(animalTypeAnimalOld.get());

        return animalService.parseToDTO(animal);
    }

    @Override
    public AnimalDTO.Response.Information deleteAnimalTypeFromAnimal(@Min(1) @NotNull Long animalId, @Min(1) @NotNull Long typeId) throws NotFoundException, BadRequestException {
        Animal animal = animalService.findById(animalId);

        var animalTypeAnimal = animalTypeAnimalDAO.findByAnimal_IdAndAnimalType_Id(animalId, typeId);

        if (animalTypeAnimal.isEmpty()) {
            throw new NotFoundException("Animal with id" + animalId + "hasn't type of animal with id " + typeId);
        } else if (animal.getAnimalTypeAnimals().size() == 1) {
            throw new BadRequestException("Animal with id" + animalId + "has only one type of animal with id " + typeId);
        }

        animalTypeAnimal.ifPresent(animalTypeAnimalDAO::delete);

        return animalService.parseToDTO(animal);
    }
}
