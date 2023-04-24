package com.example.dripchip.controller;

import com.example.dripchip.dto.AnimalDTO.Response;
import com.example.dripchip.dto.AnimalDTO.Request;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<Response.Information> registrationAnimal(@RequestBody Request.Registration registration)
            throws ConflictException, NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(animalService.registration(registration));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Response.Information>> searchAnimal(
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(required = false) Integer chipperId,
            @RequestParam(required = false) Long chippingLocationId,
            @RequestParam(required = false) String lifeStatus,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) throws ParseException {

        Request.Search searchDTO = Request.Search.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .chipperId(chipperId)
                .chippingLocationId(chippingLocationId)
                .lifeStatus(lifeStatus)
                .gender(gender)
                .from(from)
                .size(size)
                .build();

        return ResponseEntity.ok(animalService.search(searchDTO));

    }

    @GetMapping("{animalId}")
    public ResponseEntity<Response.Information> getAnimalById(@PathVariable Long animalId) throws NotFoundException {
        return ResponseEntity.ok(animalService.getById(animalId));
    }

    @PutMapping("{animalId}")
    public ResponseEntity<Response.Information> updateAnimal(
            @PathVariable Long animalId,
            @RequestBody Request.Update update
    ) throws NotFoundException, ConflictException {
        return ResponseEntity.ok(animalService.update(animalId, update));
    }

    @DeleteMapping("{animalId}")
    public void deleteAnimal(@PathVariable Long animalId) throws NotFoundException, BadRequestException {
        animalService.deleteById(animalId);
    }
}
