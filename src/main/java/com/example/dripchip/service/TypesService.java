package com.example.dripchip.service;

import com.example.dripchip.dto.AnimalsTypesDTO;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface TypesService {
    AnimalsTypesDTO.Response.AnimalsTypes addAnimalsTypes(@Valid AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) throws ConflictException;

    AnimalsTypesDTO.Response.AnimalsTypes getAnimalsTypesById(@NotNull @Min(1) Long typeId) throws NotFoundException;

    AnimalsTypesDTO.Response.AnimalsTypes putAnimalsTypesById(Long typeId, AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) throws ConflictException, NotFoundException;

    void deleteAnimalsTypesById(@Min(1) @NotNull  Long typeId) throws BadRequestException, NotFoundException;

    void save(AnimalType animalType);

    AnimalType getEntityAnimalsTypesById(Long typeId) throws NotFoundException;
}
