package com.example.dripchip.service;

import com.example.dripchip.dto.AnimalsDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface AnimalService {

    Animal registerAnimal(@Valid AnimalsDTO.Request.Registration registration) throws ConflictException, NotFoundException;

    Animal getAnimalById(@Min(1) @NotNull Long animalId) throws NotFoundException;

    Animal update(@NotNull @Min(1) Long animalId, @Valid AnimalsDTO.Request.Update update) throws NotFoundException;

    void deleteAnimalById(@NotNull @Min(0) Long animalId) throws NotFoundException, BadRequestException;
}
