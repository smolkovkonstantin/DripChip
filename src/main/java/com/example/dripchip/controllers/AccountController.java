package com.example.dripchip.controllers;

import com.example.dripchip.dto.AccountDTO;
import com.example.dripchip.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Validated
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDTO.Response.Information> getInformationAboutAccountById
            (@PathVariable @NotBlank @Min(1) String accountId) {
        return accountService.getInformationAboutAccountById(Integer.parseInt(accountId));
    }


    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDTO.Response.UpdateAccount> updateAccountById(
            @PathVariable @NotBlank @Min(1) Integer accountId, @RequestBody @Valid AccountDTO.Request.UpdateAccount updateAccount) {
        return accountService.updateAccountById(accountId, updateAccount);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<AccountDTO.Response.Empty> deleteAccountById(@PathVariable @NotBlank @Min(1) Integer accountId) {
        return accountService.deleteAccountById(accountId);
    }
}
