package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AnimalsTypesDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositorie.AnimalDAO;
import com.example.dripchip.repositorie.AnimalTypeAnimalDAO;
import com.example.dripchip.repositorie.AnimalsTypesDAO;
import com.example.dripchip.service.TypesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class TypesServiceImpl implements TypesService {

    private final AnimalsTypesDAO animalsTypesDAO;
    private final AnimalDAO animalDAO;
    private final AnimalTypeAnimalDAO animalTypeAnimalDAO;

    @Override
    public AnimalsTypesDTO.Response.AnimalsTypes addAnimalsTypes(@Valid AnimalsTypesDTO.Request.AnimalsTypes animalsTypesDTO) throws ConflictException {

        Optional<AnimalType> opAnimalsTypes = animalsTypesDAO.findByType(animalsTypesDTO.getType());

        if (opAnimalsTypes.isPresent()) {
            throw new ConflictException("Animal type with this type already exists");
        }

        AnimalType animalsTypes = AnimalType.builder()
                .type(animalsTypesDTO.getType())
                .build();

        animalsTypesDAO.save(animalsTypes);

        return parseToDTO(animalsTypes);
    }

    @Override
    public AnimalsTypesDTO.Response.AnimalsTypes getAnimalsTypesById(@NotNull @Min(1) Long typeId) throws NotFoundException {

        AnimalType animalsTypes = animalsTypesDAO.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Animal with type id not found"));

        return parseToDTO(animalsTypes);
    }

    @Override
    public AnimalsTypesDTO.Response.AnimalsTypes putAnimalsTypesById(Long typeId, AnimalsTypesDTO.Request.AnimalsTypes animalsTypesDTO) throws ConflictException, NotFoundException {

        AnimalType animalType = animalsTypesDAO.findById(typeId).orElseThrow(() -> new NotFoundException("Animal with type id not found"));

        var opAnimalTypeByType = animalsTypesDAO.findByType(animalsTypesDTO.getType());

        if (opAnimalTypeByType.isPresent()) {
            throw new ConflictException("This animals type has already exists");
        }

        animalType.setType(animalsTypesDTO.getType());

        animalsTypesDAO.updateTypeById(animalsTypesDTO.getType(), typeId);

        return parseToDTO(animalType);
    }

    @Override
    public void deleteAnimalsTypesById(@Min(1) @NotNull Long typeId) throws NotFoundException, BadRequestException {
        AnimalType animalType = animalsTypesDAO.findById(typeId).orElseThrow(() -> new NotFoundException("Animal type not found"));
        var animals = animalDAO.findByAnimalTypeAnimals_AnimalType_Id(animalType.getId());

        if (!animals.isEmpty()) {
            throw new BadRequestException("Animal with id " + animals.stream().map(Animal::getId).toList() + " has type with id: " + typeId);
        }

        animalTypeAnimalDAO.deleteAll(animalType.getAnimalTypeAnimals());

        animalsTypesDAO.deleteById(typeId);
    }

    @Override
    public void save(AnimalType animalType) {
        animalsTypesDAO.save(animalType);
        animalsTypesDAO.flush();
    }

    @Override
    public AnimalType getEntityAnimalsTypesById(Long typeId) throws NotFoundException {
        return animalsTypesDAO.findById(typeId).orElseThrow(() -> new NotFoundException("Animal type not found with with id: " + typeId));
    }

    private AnimalsTypesDTO.Response.AnimalsTypes parseToDTO(AnimalType animalTypes) {
        return AnimalsTypesDTO.Response.AnimalsTypes
                .builder()
                .id(animalTypes.getId())
                .type(animalTypes.getType())
                .build();
    }
}
