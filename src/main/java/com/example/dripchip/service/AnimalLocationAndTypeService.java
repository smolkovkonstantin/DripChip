package com.example.dripchip.service;

import com.example.dripchip.entites.Animal;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface AnimalLocationAndTypeService {
    Animal addAnimalTypeToAnimal(@Min(0) @NotNull Long animalId, @Min(0) @NotNull Long typesId) throws NotFoundException, ConflictException;
}
