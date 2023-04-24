package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AccountDTO.Request;
import com.example.dripchip.dto.AccountDTO.Response;
import com.example.dripchip.dto.RegistrationDTO;
import com.example.dripchip.entites.Account;
import com.example.dripchip.entites.Role;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.ForbiddenException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositorie.AccountDAO;
import com.example.dripchip.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;


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
    public Account getAccountById(@Min(1) @NotNull Integer id) throws NotFoundException {
        return accountDAO.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found with with id: " + id));
    }

    @Override
    public List<Response.Information> findInformationAboutAccountBySearch(Request.SearchAccount searchDTO) {

        var accounts = accountDAO.searchAccountByFirstNameAndLastNameAndEmail(
                searchDTO.getFirstName(), searchDTO.getLastName(), searchDTO.getEmail(), PageRequest.ofSize(searchDTO.getSize())
        );

        return accounts.stream().skip(searchDTO.getFrom()).map(this::parseToDTO).toList();
    }

    @Override
    public Response.Information updateAccountById
            (@Min(1) @NotNull Integer accountId,
             @Valid Request.UpdateAccount updateAccount)
            throws ConflictException, ForbiddenException, NotFoundException {

        Account account = getAccountById(accountId);

        if (!doesTheUserManageTheirAccount(accountId)) {
            throw new ForbiddenException("User can't update account another user");
        }

        Optional<Account> accountByEmail = accountDAO.findByEmail(updateAccount.getEmail());

        if (accountByEmail.isPresent() && !accountByEmail.get().getEmail().equals(updateAccount.getEmail())) {
            throw new ConflictException("This email already taken");
        }

        account.setEmail(updateAccount.getEmail());
        account.setFirstName(updateAccount.getFirstName());
        account.setLastName(updateAccount.getLastName());
        account.setPassword(bCryptPasswordEncoder.encode(updateAccount.getPassword()));

        return parseToDTO(account);
    }

    @Override
    public void deleteAccountById(@Min(1) @NotNull Integer accountId)
            throws ForbiddenException, BadRequestException, NotFoundException {

        Account account = getAccountById(accountId);

        if (!doesTheUserManageTheirAccount(accountId)) {
            throw new ForbiddenException("User can't update account another user or account hasn't found");
        }

        if (account.getAnimals() == null || account.getAnimals().size() > 0) {
            throw new BadRequestException("User has information about animal in the account");
        }

        accountDAO.delete(account);
    }

    private boolean doesTheUserManageTheirAccount(int id) {
        Account userAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userAccount.getId().equals(id);
    }

    @Override
    public Response.Information parseToDTO(Account account) {
        return Response.Information.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .build();
    }
}
