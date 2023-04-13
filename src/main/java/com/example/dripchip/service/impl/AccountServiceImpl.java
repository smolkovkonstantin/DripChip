package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AccountDTO.Request;
import com.example.dripchip.dto.AccountDTO.Response;
import com.example.dripchip.dto.RegistrationDTO;
import com.example.dripchip.entites.Account;
import com.example.dripchip.entites.Role;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.ForbiddenException;
import com.example.dripchip.repositories.AccountDAO;
import com.example.dripchip.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.stream.Stream;


@Service
@Validated
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<RegistrationDTO.Response.Registration> register(@Valid RegistrationDTO.Request.Registration registrationDTO)
            throws ConflictException {

        if (accountDAO.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new ConflictException("this user is already exists");
        }

        Account account = Account.builder()
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .email(registrationDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(registrationDTO.getPassword()))
                .role(Role.USER)
                .build();

        accountDAO.save(account);

        return Optional.ofNullable(RegistrationDTO.Response.Registration.builder()
                .id(account.getId())
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .email(registrationDTO.getEmail())
                .build());
    }

    @Override
    public Optional<Account> getAccountById(@Min(1) @NotNull Integer id) {
        return accountDAO.findById(id);
    }

    @Override
    public Optional<Stream<Account>> findInformationAboutAccountBySearch(
            @Valid Request.SearchAccount searchDTO) {

        var accounts = accountDAO.searchAccountByFirstNameAndLastNameAndEmail(
                searchDTO.getFirstName(), searchDTO.getLastName(), searchDTO.getEmail(), PageRequest.ofSize(searchDTO.getSize())
        );

        return Optional.of(accounts.stream().skip(searchDTO.getFrom()));
    }

    @Override
    public Optional<Response.UpdateAccount> updateAccountById
            (@Min(1) @NotNull Integer accountId,
             @Valid Request.UpdateAccount updateAccount)
            throws ConflictException, ForbiddenException {

        Optional<Account> opAccountById = accountDAO.findById(accountId);

        if (opAccountById.isEmpty() || !doesTheUserManageTheirAccount(accountId)) {
            throw new ForbiddenException("User can't update account another user or account hasn't found");
        }

        if (accountDAO.findByEmail(updateAccount.getEmail()).isPresent()) {
            throw new ConflictException("");
        }

        accountDAO.updateFirstNameAndLastNameAndPasswordAndEmailById(
                updateAccount.getFirstName(), updateAccount.getLastName(),
                bCryptPasswordEncoder.encode(updateAccount.getPassword()), updateAccount.getEmail(),
                accountId);

        return Optional.of(
                Response.UpdateAccount.builder()
                        .id(accountId)
                        .firstName(updateAccount.getFirstName())
                        .lastName(updateAccount.getLastName())
                        .email(updateAccount.getEmail())
                        .build()
        );
    }

    @Override
    public void deleteAccountById(@Min(1) @NotNull Integer accountId)
            throws ForbiddenException, BadRequestException {

        Optional<Account> opAccountById = accountDAO.findById(accountId);

        if (!doesTheUserManageTheirAccount(accountId) || opAccountById.isEmpty()) {
            throw new ForbiddenException("User can't update account another user or account hasn't found");
        }

        if (opAccountById.get().getAnimals() == null || opAccountById.get().getAnimals().size() > 0) {
            throw new BadRequestException("User has information about animal in the account");
        }
    }

    private boolean doesTheUserManageTheirAccount(int id) {
        Account userAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userAccount.getId().equals(id);
    }
}
