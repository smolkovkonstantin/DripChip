package com.example.dripchip.repositories;

import com.example.dripchip.entites.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
}
