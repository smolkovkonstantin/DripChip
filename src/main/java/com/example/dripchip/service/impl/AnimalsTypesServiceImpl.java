package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AnimalsTypesDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.AnimalsType;
import com.example.dripchip.repositories.AnimalDAO;
import com.example.dripchip.repositories.AnimalsTypesDAO;
import com.example.dripchip.service.AnimalsTypesService;
import com.example.dripchip.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimalsTypesServiceImpl implements AnimalsTypesService {

    private final AnimalsTypesDAO animalsTypesDAO;

    private final AnimalDAO animalDAO;

    @Override
    public ResponseEntity<AnimalsTypesDTO.Response.AnimalsTypes> addAnimalsTypes(AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) {
        if (StringUtil.isWhitespace(animalsTypes.getType())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<AnimalsType> opAnimalsTypes = animalsTypesDAO.findByType(animalsTypes.getType());

        if (opAnimalsTypes.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        AnimalsType newAnimalsTypes = AnimalsType.builder()
                .type(animalsTypes.getType())
                .build();

        animalsTypesDAO.save(newAnimalsTypes);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AnimalsTypesDTO.Response.AnimalsTypes
                        .builder()
                        .id(newAnimalsTypes.getId())
                        .type(animalsTypes.getType())
                        .build()
        );
    }

    @Override
    public ResponseEntity<AnimalsTypesDTO.Response.AnimalsTypes> getAnimalsTypesById(Long typeId) {
        if (typeId == null || typeId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<AnimalsType> opAnimalsTypes = animalsTypesDAO.findById(typeId);

        return opAnimalsTypes.map(location -> ResponseEntity.ok().body(AnimalsTypesDTO.Response.AnimalsTypes
                .builder()
                .id(location.getId())
                .type(opAnimalsTypes.get().getType())
                .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<AnimalsTypesDTO.Response.AnimalsTypes> putAnimalsTypesById(Long typeId, AnimalsTypesDTO.Request.AnimalsTypes animalsTypes) {
        if (StringUtil.isWhitespace(animalsTypes.getType()) || typeId == null || typeId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<AnimalsType> opAnimalsTypesById = animalsTypesDAO.findById(typeId);


        if (opAnimalsTypesById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<AnimalsType> opAnimalsTypesByType = animalsTypesDAO.findByType(animalsTypes.getType());

        if (opAnimalsTypesByType.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        animalsTypesDAO.updateTypeById(animalsTypes.getType(), typeId);

        return ResponseEntity.ok().body(
                AnimalsTypesDTO.Response.AnimalsTypes
                        .builder()
                        .id(typeId)
                        .type(animalsTypes.getType())
                        .build()
        );
    }

    @Override
    public ResponseEntity<AnimalsTypesDTO.Response.Empty> deleteAnimalsTypesById(Long typeId) {
        Optional<Animal> opAnimal = animalDAO.findByAnimalsTypeId(typeId);

        if (opAnimal.isPresent() || typeId == null || typeId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<AnimalsType> opAnimalsTypes = animalsTypesDAO.findById(typeId);

        if (opAnimalsTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        animalsTypesDAO.deleteById(typeId);

        return ResponseEntity.ok().body(new AnimalsTypesDTO.Response.Empty());
    }
}
