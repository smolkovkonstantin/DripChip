package com.example.dripchip.service;

import com.example.dripchip.dto.AccountDTO;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<AccountDTO.Response.Information> getInformationAboutAccountById(Integer id);

    ResponseEntity<AccountDTO.Response.UpdateAccount> updateAccountById(Integer accountId, AccountDTO.Request.UpdateAccount updateAccount);

    ResponseEntity<AccountDTO.Response.Empty> deleteAccountById(Integer accountId);
}
