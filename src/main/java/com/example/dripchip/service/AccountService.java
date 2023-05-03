package com.example.dripchip.service;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.dto.RegistrationDTO;
import com.example.dripchip.entites.Account;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.ForbiddenException;
import com.example.dripchip.exception.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDTO.Response.Information register(@Valid RegistrationDTO.Request.Registration registrationDTO) throws ConflictException;

    Optional<Account> getAccountById(@Min(1) @NotNull Integer id);

    List<AccountDTO.Response.Information> findInformationAboutAccountBySearch(AccountDTO.Request.SearchAccount searchDTO) throws BadRequestException;

    AccountDTO.Response.Information updateAccountById(@Min(1) @NotNull Integer accountId, @Valid AccountDTO.Request.UpdateAccount updateAccount) throws ConflictException, ForbiddenException, NotFoundException;

    void deleteAccountById(@Min(1) @NotNull Integer accountId) throws ForbiddenException, BadRequestException, NotFoundException;

    AccountDTO.Response.Information parseToDTO(Account account);
}
