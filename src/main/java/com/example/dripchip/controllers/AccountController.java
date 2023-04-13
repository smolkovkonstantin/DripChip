package com.example.dripchip.controllers;

import com.example.dripchip.dto.AccountDTO.Response;
import com.example.dripchip.dto.AccountDTO.Request;
import com.example.dripchip.entites.Account;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.ForbiddenException;
import com.example.dripchip.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getInformationAboutAccountById
            (@PathVariable Integer accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Stream<Account>> getInformationAboutAccountBySearch
            (
                    @RequestParam(required = false) String firstName,
                    @RequestParam(required = false) String lastName,
                    @RequestParam(required = false) String email,
                    @RequestParam(required = false, defaultValue = "0") Integer from,
                    @RequestParam(required = false, defaultValue = "10") Integer size
            ) {

        var searchDTO = Request.SearchAccount
                .builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .from(from)
                .size(size)
                .build();
        var opAccounts = accountService.findInformationAboutAccountBySearch(searchDTO);

        return opAccounts.map(ResponseEntity::ok).orElse(ResponseEntity.ok().build());
    }


    @PutMapping("/{accountId}")
    public ResponseEntity<Response.UpdateAccount> updateAccountById(
            @PathVariable Integer accountId, @RequestBody Request.UpdateAccount updateAccount
    ) throws ForbiddenException, ConflictException {
        return accountService.updateAccountById(accountId, updateAccount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccountById(@PathVariable Integer accountId) throws ForbiddenException, BadRequestException {
        accountService.deleteAccountById(accountId);
    }
}
