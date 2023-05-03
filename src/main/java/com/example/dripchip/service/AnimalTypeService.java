package com.example.dripchip.service;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface AnimalTypeService {

    AnimalDTO.Response.Information addAnimalTypeToAnimal(@Min(1) @NotNull Long animalId, @Min(1) @NotNull Long typeId) throws NotFoundException, ConflictException;

    AnimalDTO.Response.Information updateAnimalTypeInAnimal(@NotNull @Min(1) Long animalId, @Valid AnimalDTO.Request.UpdateAnimalType update) throws NotFoundException, ConflictException;

    AnimalDTO.Response.Information deleteAnimalTypeFromAnimal(@NotNull @Min(1) Long animalId, @NotNull @Min(1) Long typeId) throws NotFoundException, BadRequestException;

}
