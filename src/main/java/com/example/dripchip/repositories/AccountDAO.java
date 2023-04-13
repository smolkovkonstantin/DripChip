package com.example.dripchip.repositories;

import com.example.dripchip.entites.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    @Query(value = "select * from Account where (lower(first_name) like '%' || lower(?1) || '%' or ?1 is null)" +
            " and (lower(last_name) like '%' || lower(?2) || '%' or ?2 is null)" +
            " and (lower(email) like '%' || lower(?3) || '%' or ?3 is null)" +
            " order by id_account", nativeQuery = true)
    Page<Account> searchAccountByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Account a set a.firstName = ?1, a.lastName = ?2, a.password = ?3, a.email = ?4 where a.id = ?5")
    void updateFirstNameAndLastNameAndPasswordAndEmailById(String firstName, String lastName, String password, String email, Integer id);

    Optional<Account> findByEmail(String email);

    @NotNull Optional<Account> findById(@NotNull Integer id);
}
