package com.example.dripchip.service;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.text.ParseException;
import java.util.List;

public interface AnimalService {

    AnimalDTO.Response.Information registration(@Valid AnimalDTO.Request.Registration registration) throws ConflictException, NotFoundException;

    AnimalDTO.Response.Information getById(@Min(1) @NotNull Long animalId) throws NotFoundException;

    AnimalDTO.Response.Information update(@NotNull @Min(1) Long animalId, @Valid AnimalDTO.Request.Update update) throws NotFoundException, BadRequestException;

    void deleteById(@NotNull @Min(1) Long animalId) throws NotFoundException, BadRequestException;

    AnimalDTO.Response.Information parseToDTO(Animal animal);

    Animal findById(@NotNull @Min(1) Long animalId) throws NotFoundException;

    void saveAnimal(Animal animal);

    List<AnimalDTO.Response.Information> search(AnimalDTO.Request.Search searchDTO) throws ParseException, BadRequestException;
}
