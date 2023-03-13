package com.example.dripchip.service.impl;

import com.example.dripchip.dto.RegistrationDTO.Response;
import com.example.dripchip.dto.RegistrationDTO.Request;
import com.example.dripchip.entites.Account;
import com.example.dripchip.entites.Role;
import com.example.dripchip.repositories.AccountDAO;
import com.example.dripchip.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final AccountDAO accountDAO;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<Response.Registration> register(Request.Registration registrationDTO) {

        if (accountDAO.findByEmail(registrationDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Account account = Account.builder()
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .email(registrationDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(registrationDTO.getPassword()))
                .role(Role.USER)
                .build();

        accountDAO.save(account);

        return ResponseEntity.status(HttpStatus.CREATED).body(Response.Registration.builder()
                .id(account.getId())
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .email(registrationDTO.getEmail())
                .build()
        );
    }
}
