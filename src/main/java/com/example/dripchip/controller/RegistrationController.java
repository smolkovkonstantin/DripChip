package com.example.dripchip.controller;


import com.example.dripchip.dto.RegistrationDTO.Request;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> registrationAccount(
            @RequestBody Request.Registration registrationDTO) throws ConflictException {
        return ResponseEntity.of(accountService.register(registrationDTO));
    }

}
