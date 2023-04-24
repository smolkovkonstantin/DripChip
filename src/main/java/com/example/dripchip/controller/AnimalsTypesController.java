package com.example.dripchip.controller;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.dto.AnimalsTypesDTO.Request;
import com.example.dripchip.dto.AnimalsTypesDTO.Response;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalsTypesController {
    private final TypesService typesService;
    private final AnimalTypeService animalTypeService;
    private final AnimalService animalService;

    @PostMapping("/types")
    public ResponseEntity<Response.AnimalsTypes> addAnimalsTypes(@RequestBody Request.AnimalsTypes animalsTypes) throws ConflictException {
        return ResponseEntity.ok(typesService.addAnimalsTypes(animalsTypes));
    }

    @GetMapping("/types/{typeId}")
    public ResponseEntity<Response.AnimalsTypes> getAnimalsTypes(@PathVariable Long typeId) {
        return ResponseEntity.ok(typesService.getAnimalsTypesById(typeId));
    }


    @PutMapping("/types/{typeId}")
    public ResponseEntity<Response.AnimalsTypes> putAnimalsTypesById(
            @PathVariable @NotNull @Min(1) Long typeId,
            @RequestBody @Valid Request.AnimalsTypes animalsTypes) throws ConflictException {
        return ResponseEntity.ok(typesService.putAnimalsTypesById(typeId, animalsTypes));
    }

    @DeleteMapping("/types/{typeId}")
    public void deleteAnimalsTypesById(@PathVariable Long typeId) throws BadRequestException, NotFoundException {
        typesService.deleteAnimalsTypesById(typeId, animalService);
    }

    @PostMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalDTO.Response.Information> addAnimalType(@PathVariable Long animalId, @PathVariable Long typeId) throws ConflictException, NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(animalTypeService.addAnimalTypeToAnimal(animalId, typeId));
    }

    @PutMapping("/{animalId}/types")
    public ResponseEntity<AnimalDTO.Response.Information> updateAnimalType(@PathVariable Long animalId, @RequestBody AnimalDTO.Request.UpdateAnimalType update) throws ConflictException, NotFoundException {
        return ResponseEntity.ok(animalTypeService.updateAnimalTypeInAnimal(animalId, update));
    }

    @DeleteMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalDTO.Response.Information> deleteAnimalType(@PathVariable Long animalId, @PathVariable Long typeId) throws NotFoundException, BadRequestException {
        return ResponseEntity.ok(animalTypeService.deleteAnimalTypeFromAnimal(animalId, typeId));
    }
}
