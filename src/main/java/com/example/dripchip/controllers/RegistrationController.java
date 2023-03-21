package com.example.dripchip.controllers;


import com.example.dripchip.dto.RegistrationDTO.Response;
import com.example.dripchip.dto.RegistrationDTO.Request;
import com.example.dripchip.service.impl.RegistrationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationServiceImpl accountServices;

    @Autowired
    public RegistrationController(RegistrationServiceImpl accountServices) {
        this.accountServices = accountServices;
    }

    @PostMapping
    public ResponseEntity<Response.Registration> registrationAccount(
            @RequestBody @Valid Request.Registration registrationDTO) {

        return accountServices.register(registrationDTO);
    }

}
