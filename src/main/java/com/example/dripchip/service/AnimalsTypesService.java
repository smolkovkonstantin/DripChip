package com.example.dripchip.service;

import com.example.dripchip.dto.AnimalsTypesDTO;
import org.springframework.http.ResponseEntity;

public interface AnimalsTypesService {
    ResponseEntity<AnimalsTypesDTO.Response.AnimalsTypes> addAnimalsTypes(AnimalsTypesDTO.Request.AnimalsTypes animalsTypes);

    ResponseEntity<AnimalsTypesDTO.Response.AnimalsTypes> getAnimalsTypesById(Long typeId);

    ResponseEntity<AnimalsTypesDTO.Response.AnimalsTypes> putAnimalsTypesById(Long typeId, AnimalsTypesDTO.Request.AnimalsTypes animalsTypes);

    ResponseEntity<AnimalsTypesDTO.Response.Empty> deleteAnimalsTypesById(Long typeId);
}
