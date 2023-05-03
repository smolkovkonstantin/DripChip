package com.example.dripchip.repositorie;

import com.example.dripchip.entites.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountDAO extends JpaRepository<Account, Integer> {
    @Query(value = "select * from Account where (lower(first_name) like '%' || lower(?1) || '%' or ?1 is null)" +
            " and (lower(last_name) like '%' || lower(?2) || '%' or ?2 is null)" +
            " and (lower(email) like '%' || lower(?3) || '%' or ?3 is null)" +
            " order by id_account", nativeQuery = true)
    List<Account> searchAccountByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);

    Optional<Account> findByEmail(String email);

    @NotNull Optional<Account> findById(@NotNull Integer id);
}
