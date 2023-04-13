package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalsTypesDTO.Request;
import com.example.dripchip.dto.AnimalsTypesDTO.Response;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
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
public class AnimalsTypesController {
    private final AnimalsTypesService animalsTypesService;

    @PostMapping
    public ResponseEntity<Response.AnimalsTypes> addAnimalsTypes(@RequestBody Request.AnimalsTypes animalsTypes) throws ConflictException {
        return ResponseEntity.ok(animalsTypesService.addAnimalsTypes(animalsTypes));
    }

    @GetMapping("{typeId}")
    public ResponseEntity<Response.AnimalsTypes> getAnimalsTypes(@PathVariable Long typeId) {
        return ResponseEntity.ok(animalsTypesService.getAnimalsTypesById(typeId));
    }


    @PutMapping("{typeId}")
    public ResponseEntity<Response.AnimalsTypes> putAnimalsTypesById(
            @PathVariable @NotNull @Min(1) Long typeId,
            @RequestBody @Valid Request.AnimalsTypes animalsTypes) throws ConflictException {
        return ResponseEntity.ok(animalsTypesService.putAnimalsTypesById(typeId, animalsTypes));
    }

    @DeleteMapping("{typeId}")
    public void deleteAnimalsTypesById(@PathVariable Long typeId) throws BadRequestException, NotFoundException {
        animalsTypesService.deleteAnimalsTypesById(typeId);
    }
}
