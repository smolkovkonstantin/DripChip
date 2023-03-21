package com.example.dripchip.service.impl;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.entites.Account;
import com.example.dripchip.repositories.AccountDAO;
import com.example.dripchip.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<AccountDTO.Response.Information> getInformationAboutAccountById(Integer id) {

        Optional<Account> OpAccount = accountDAO.findById(id);

        if (OpAccount.isPresent()) {

            Account account = OpAccount.get();

            return ResponseEntity.ok().body(
                    AccountDTO.Response.Information.builder()
                            .id(account.getId())
                            .firstName(account.getFirstName())
                            .lastName(account.getLastName())
                            .email(account.getEmail())
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<AccountDTO.Response.UpdateAccount> updateAccountById(Integer accountId, AccountDTO.Request.UpdateAccount updateAccount) {

        Optional<Account> opAccountById = accountDAO.findById(accountId);

        if (opAccountById.isEmpty() || !doesTheUserManageTheirAccount(accountId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (accountDAO.findByEmail(updateAccount.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        accountDAO.updateFirstNameAndLastNameAndPasswordAndEmailById(
                updateAccount.getFirstName(), updateAccount.getLastName(),
                bCryptPasswordEncoder.encode(updateAccount.getPassword()), updateAccount.getEmail(),
                accountId);

        return ResponseEntity.ok().body(
                AccountDTO.Response.UpdateAccount.builder()
                        .id(accountId)
                        .firstName(updateAccount.getFirstName())
                        .lastName(updateAccount.getLastName())
                        .email(updateAccount.getEmail())
                        .build()
        );
    }

    @Override
    public ResponseEntity<AccountDTO.Response.Empty> deleteAccountById(Integer accountId) {

        Optional<Account> opAccountById = accountDAO.findById(accountId);

        if (!doesTheUserManageTheirAccount(accountId) || opAccountById.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (opAccountById.get().getAnimals() == null || opAccountById.get().getAnimals().size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }


    private boolean doesTheUserManageTheirAccount(int id) {
        Account userAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userAccount.getId().equals(id);
    }
}
