package com.example.dripchip.controllers;

import com.example.dripchip.entites.Animal;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.AnimalLocationAndTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/animals/{animalId}")
@RequiredArgsConstructor
public class AnimalLocationAndTypeController {

    private AnimalLocationAndTypeService animalLocationAndTypeService;

    @PostMapping("/types/{typesId}")
    public ResponseEntity<Animal> addAnimalTypeToAnimal(@PathVariable Long animalId, @PathVariable Long typesId) throws ConflictException, NotFoundException {
        return ResponseEntity.ok(animalLocationAndTypeService.addAnimalTypeToAnimal(animalId, typesId));
    }

}
