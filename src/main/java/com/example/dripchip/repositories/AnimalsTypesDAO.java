package com.example.dripchip.repositories;

import com.example.dripchip.entites.AnimalsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AnimalsTypesDAO extends JpaRepository<AnimalsType, Long> {
    @Transactional
    @Modifying
    @Query("update AnimalsType a set a.type = ?1 where a.id = ?2")
    void updateTypeById(String type, Long id);

    Optional<AnimalsType> findByType(String type);
}
