package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AccountDTO.Request;
import com.example.dripchip.dto.AccountDTO.Response;
import com.example.dripchip.dto.RegistrationDTO;
import com.example.dripchip.entites.Account;
import com.example.dripchip.entites.Role;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.ForbiddenException;
import com.example.dripchip.repositorie.AccountDAO;
import com.example.dripchip.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
    public Response.Information register(@Valid RegistrationDTO.Request.Registration registrationDTO)
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

        return parseToDTO(account);
    }

    @Override
    public Optional<Account> getAccountById(@Min(1) @NotNull Integer id) {
        return accountDAO.findById(id);
    }

    @Override
    public List<Response.Information> findInformationAboutAccountBySearch(Request.SearchAccount searchDTO) throws BadRequestException {

        if (searchDTO.getFrom() < 0 || searchDTO.getSize() <= 0) {
            throw new BadRequestException("From must can't be less than 1 and size can't be less and equal than 0");
        }

        var accounts = accountDAO.searchAccountByFirstNameAndLastNameAndEmail(
                searchDTO.getFirstName(), searchDTO.getLastName(), searchDTO.getEmail()
        );

        return accounts.stream().skip(searchDTO.getFrom()).limit(searchDTO.getSize()).map(this::parseToDTO).toList();
    }

    @Override
    public Response.Information updateAccountById
            (@Min(1) @NotNull Integer accountId,
             @Valid Request.UpdateAccount updateDTO)
            throws ConflictException, ForbiddenException {

        Account account = getAccountById(accountId).orElseThrow(() -> new ForbiddenException("Account doesn't exists"));

        if (userManageAnotherAccount(accountId)) {
            throw new ForbiddenException("User can't update account another user");
        }

        Optional<Account> accountByEmail = accountDAO.findByEmail(updateDTO.getEmail());

        if (accountByEmail.isPresent() && !accountByEmail.get().equals(account)) {
            throw new ConflictException("This email already taken");
        }

        account.setEmail(updateDTO.getEmail());
        account.setFirstName(updateDTO.getFirstName());
        account.setLastName(updateDTO.getLastName());
        account.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));

        accountDAO.save(account);

        return parseToDTO(account);
    }

    @Override
    public void deleteAccountById(@Min(1) @NotNull Integer accountId)
            throws ForbiddenException, BadRequestException {

        Account account = getAccountById(accountId).orElseThrow(() -> new ForbiddenException("Account doesn't exists"));

        if (userManageAnotherAccount(accountId)) {
            throw new ForbiddenException("User can't update account another user or account hasn't found");
        }

        if (account.getAnimals() == null || !account.getAnimals().isEmpty()) {
            throw new BadRequestException("User has information about animal in the account");
        }

        accountDAO.delete(account);
    }

    private boolean userManageAnotherAccount(int id) {
        Account userAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return !userAccount.getId().equals(id);
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
