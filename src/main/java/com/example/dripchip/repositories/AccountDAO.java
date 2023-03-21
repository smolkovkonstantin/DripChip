package com.example.dripchip.repositories;

import com.example.dripchip.entites.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    @Transactional
    @Modifying
    @Query("update Account a set a.firstName = ?1, a.lastName = ?2, a.password = ?3, a.email = ?4 where a.id = ?5")
    void updateFirstNameAndLastNameAndPasswordAndEmailById(String firstName, String lastName, String password, String email, Integer id);
    Optional<Account> findByEmail(String email);

    Optional<Account> findById(Integer id);
}
