package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalsDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<Animal> registrationAnimal(@RequestBody AnimalsDTO.Request.Registration registration)
            throws ConflictException, NotFoundException {
        return ResponseEntity.ok(animalService.registerAnimal(registration));
    }

    @GetMapping("{animalId}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long animalId) throws NotFoundException {
        return ResponseEntity.ok(animalService.getAnimalById(animalId));
    }

    @PutMapping("{animalId}")
    public ResponseEntity<Animal> updateAnimal(
            @PathVariable Long animalId,
            @RequestBody AnimalsDTO.Request.Update update
    ) throws NotFoundException {
        return ResponseEntity.ok(animalService.update(animalId, update));
    }

    @DeleteMapping("{animalId}")
    public void deleteAnimal(@PathVariable Long animalId) throws NotFoundException, BadRequestException {
        animalService.deleteAnimalById(animalId);
    }
}
