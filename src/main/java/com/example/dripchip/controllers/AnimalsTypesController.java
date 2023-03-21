package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalsTypesDTO.Request;
import com.example.dripchip.dto.AnimalsTypesDTO.Response;
import com.example.dripchip.service.AnimalsTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/types")
@RequiredArgsConstructor
public class AnimalsTypesController {
    private final AnimalsTypesService animalsTypesService;

    @PostMapping
    public ResponseEntity<Response.AnimalsTypes> addAnimalsTypes(@RequestBody Request.AnimalsTypes animalsTypes) {
        return animalsTypesService.addAnimalsTypes(animalsTypes);
    }

    @GetMapping("{typeId}")
    public ResponseEntity<Response.AnimalsTypes> getAnimalsTypes(@PathVariable Long typeId) {
        return animalsTypesService.getAnimalsTypesById(typeId);
    }


    @PutMapping("{typeId}")
    public ResponseEntity<Response.AnimalsTypes> putAnimalsTypesById(
            @PathVariable Long typeId, @RequestBody Request.AnimalsTypes animalsTypes) {
        return animalsTypesService.putAnimalsTypesById(typeId, animalsTypes);
    }

    @DeleteMapping("{typeId}")
    public ResponseEntity<Response.Empty> deleteAnimalsTypesById(@PathVariable Long typeId) {
        return animalsTypesService.deleteAnimalsTypesById(typeId);
    }

}
