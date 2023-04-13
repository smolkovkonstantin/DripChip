package com.example.dripchip.service.impl;

import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositories.AnimalDAO;
import com.example.dripchip.repositories.AnimalsTypesDAO;
import com.example.dripchip.service.AnimalLocationAndTypeService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class AnimalLocationAndTypeServiceImpl implements AnimalLocationAndTypeService {


    private AnimalDAO animalDAO;
    private AnimalsTypesDAO animalsTypesDAO;

    @Override
    public Animal addAnimalTypeToAnimal(@Min(0) @NotNull Long animalId, @Min(0) @NotNull Long typesId) throws NotFoundException, ConflictException {

        var opAnimal = animalDAO.findById(animalId);
        var opAnimalType = animalsTypesDAO.findById(typesId);

        if (opAnimal.isEmpty() || opAnimalType.isEmpty()) {
            throw new NotFoundException("Not found animal or animal type by id");
        }

        if (opAnimal.get().getAnimalTypes().contains(opAnimalType.get())) {
            throw new ConflictException("Animal already has this type");
        }

        opAnimal.get().getAnimalTypes().add(opAnimalType.get());

        animalDAO.updateAnimalTypesById(opAnimal.get().getAnimalTypes(), animalId);

        return opAnimal.get();
    }
}
