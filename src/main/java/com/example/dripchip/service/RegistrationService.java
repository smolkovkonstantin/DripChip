package com.example.dripchip.service;

import com.example.dripchip.dto.RegistrationDTO;
import org.springframework.http.ResponseEntity;

public interface RegistrationService {

    ResponseEntity<RegistrationDTO.Response.Registration> register(RegistrationDTO.Request.Registration registrationDTO);

}
