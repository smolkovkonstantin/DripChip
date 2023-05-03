package com.example.dripchip.controller;

import com.example.dripchip.dto.AccountDTO.Response;
import com.example.dripchip.dto.AccountDTO.Request;
import com.example.dripchip.entites.Account;
import com.example.dripchip.exception.BadRequestException;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.ForbiddenException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<Response.Information> getInformationAboutAccountById
            (@PathVariable Integer accountId) throws NotFoundException {
        Account account = accountService.getAccountById(accountId).orElseThrow(() -> new NotFoundException("Account doesn't exists"));
        return ResponseEntity.ok(accountService.parseToDTO(account));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Response.Information>> getInformationAboutAccountBySearch
            (
                    @RequestParam(required = false) String firstName,
                    @RequestParam(required = false) String lastName,
                    @RequestParam(required = false) String email,
                    @RequestParam(required = false, defaultValue = "0") Integer from,
                    @RequestParam(required = false, defaultValue = "10") Integer size
            ) throws BadRequestException {

        var searchDTO = Request.SearchAccount
                .builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .from(from)
                .size(size)
                .build();

        return ResponseEntity.ok(accountService.findInformationAboutAccountBySearch(searchDTO));
    }


    @PutMapping("/{accountId}")
    public ResponseEntity<Response.Information> updateAccountById(
            @PathVariable Integer accountId,
            @RequestBody Request.UpdateAccount updateAccount) throws ForbiddenException, ConflictException, NotFoundException {
        return ResponseEntity.ok(accountService.updateAccountById(accountId, updateAccount));
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccountById(@PathVariable Integer accountId) throws ForbiddenException, BadRequestException, NotFoundException {
        accountService.deleteAccountById(accountId);
    }
}
