package com.example.dripchip.service;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.dto.RegistrationDTO;
import com.example.dripchip.entites.Account;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.ForbiddenException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

public interface AccountService {

    Optional<RegistrationDTO.Response.Registration> register(@Valid RegistrationDTO.Request.Registration registrationDTO) throws ConflictException;

    Optional<Account> getAccountById(@Min(1) @NotNull Integer id);

    Optional<Stream<Account>> findInformationAboutAccountBySearch(@Valid AccountDTO.Request.SearchAccount searchDTO);

    Optional<AccountDTO.Response.UpdateAccount> updateAccountById(@Min(1) @NotNull Integer accountId, @Valid AccountDTO.Request.UpdateAccount updateAccount) throws ConflictException, ForbiddenException;

    void deleteAccountById(@Min(1) @NotNull Integer accountId) throws ForbiddenException, BadRequestException;
}
