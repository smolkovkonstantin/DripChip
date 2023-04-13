package com.example.dripchip.service;

import com.example.dripchip.dto.AnimalsTypesDTO;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface AnimalsTypesService {
    AnimalsTypesDTO.Response.AnimalsTypes addAnimalsTypes(AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) throws ConflictException;

    AnimalsTypesDTO.Response.AnimalsTypes getAnimalsTypesById(@NotNull @Min(1) Long typeId);

    AnimalsTypesDTO.Response.AnimalsTypes putAnimalsTypesById(Long typeId, AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) throws ConflictException;

    void deleteAnimalsTypesById(@Min(1) @NotNull  Long typeId) throws BadRequestException, NotFoundException;

    boolean isNotExists(Long animalTypeId);

    AnimalType getEntityAnimalsTypesById(Long typeId);
}
