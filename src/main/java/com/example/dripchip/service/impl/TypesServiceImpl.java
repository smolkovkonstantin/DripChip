package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AnimalsTypesDTO;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositorie.AnimalsTypesDAO;
import com.example.dripchip.service.AnimalService;
import com.example.dripchip.service.TypesService;
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

    @Override
    public AnimalsTypesDTO.Response.AnimalsTypes addAnimalsTypes(AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) throws ConflictException {

        Optional<AnimalType> opAnimalsTypes = animalsTypesDAO.findByType(animalsTypes.getType());

        if (opAnimalsTypes.isPresent()) {
            throw new ConflictException("Animal type with this type already exists");
        }

        AnimalType newAnimalsTypes = AnimalType.builder()
                .type(animalsTypes.getType())
                .build();

        animalsTypesDAO.save(newAnimalsTypes);

        return AnimalsTypesDTO.Response.AnimalsTypes
                .builder()
                .id(newAnimalsTypes.getId())
                .type(animalsTypes.getType())
                .build();
    }

    @Override
    public AnimalsTypesDTO.Response.AnimalsTypes getAnimalsTypesById(@NotNull @Min(1) Long typeId) {

        Optional<AnimalType> opAnimalsTypes = animalsTypesDAO.findById(typeId);

        return opAnimalsTypes.map(location -> AnimalsTypesDTO.Response.AnimalsTypes
                .builder()
                .id(location.getId())
                .type(opAnimalsTypes.get().getType())
                .build()).orElse(null);
    }

    @Override
    public AnimalsTypesDTO.Response.AnimalsTypes putAnimalsTypesById(Long typeId, AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) throws ConflictException {

        Optional<AnimalType> opAnimalsTypesById = animalsTypesDAO.findById(typeId);

        if (opAnimalsTypesById.isEmpty()) {
            return null;
        }

        Optional<AnimalType> opAnimalsTypesByType = animalsTypesDAO.findByType(animalsTypes.getType());

        if (opAnimalsTypesByType.isPresent()) {
            throw new ConflictException("This animals type has already exists");
        }

        animalsTypesDAO.updateTypeById(animalsTypes.getType(), typeId);

        return AnimalsTypesDTO.Response.AnimalsTypes
                .builder()
                .id(typeId)
                .type(animalsTypes.getType())
                .build();
    }

    @Override
    public void deleteAnimalsTypesById(@Min(1) @NotNull Long typeId, AnimalService animalService) throws BadRequestException, NotFoundException {
        var opAnimal = animalService.findById(typeId);

        if (opAnimal != null) {
            throw new BadRequestException("Animal with this type of animal exists");
        }

        animalsTypesDAO.findById(typeId).orElseThrow(() -> new NotFoundException("Animal type not found"));

        animalsTypesDAO.deleteById(typeId);
    }

    @Override
    public AnimalType getEntityAnimalsTypesById(Long typeId) throws NotFoundException {
        return animalsTypesDAO.findById(typeId).orElseThrow(() -> new NotFoundException("Location not found with with id: " + typeId));
    }
}
