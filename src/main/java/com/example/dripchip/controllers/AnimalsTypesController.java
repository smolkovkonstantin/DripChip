package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalsTypesDTO.Request;
import com.example.dripchip.dto.AnimalsTypesDTO.Response;
import com.example.dripchip.service.AnimalsTypesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/types")
@RequiredArgsConstructor
@Validated
public class AnimalsTypesController {
    private final AnimalsTypesService animalsTypesService;

    @PostMapping
    public ResponseEntity<Response.AnimalsTypes> addAnimalsTypes(
            @RequestBody @Valid Request.AnimalsTypes animalsTypes) {
        return animalsTypesService.addAnimalsTypes(animalsTypes);
    }

    @GetMapping("{typeId}")
    public ResponseEntity<Response.AnimalsTypes> getAnimalsTypes(
            @PathVariable @NotNull @Min(1) Long typeId) {
        return animalsTypesService.getAnimalsTypesById(typeId);
    }


    @PutMapping("{typeId}")
    public ResponseEntity<Response.AnimalsTypes> putAnimalsTypesById(
            @PathVariable @NotNull @Min(1) Long typeId,
            @RequestBody @Valid Request.AnimalsTypes animalsTypes) {
        return animalsTypesService.putAnimalsTypesById(typeId, animalsTypes);
    }

    @DeleteMapping("{typeId}")
    public ResponseEntity<Response.Empty> deleteAnimalsTypesById(
            @PathVariable @Min(1) @NotNull Long typeId) {
        return animalsTypesService.deleteAnimalsTypesById(typeId);
    }

}
