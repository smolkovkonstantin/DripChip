package com.example.dripchip.repositories;

import com.example.dripchip.entites.Account;
import com.example.dripchip.entites.Animal;
import com.example.dripchip.entites.AnimalType;
import com.example.dripchip.entites.VisitedLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AnimalDAO extends JpaRepository<Animal, Long> {
    @Transactional
    @Modifying
    @Query("update Animal a set a.animalTypes = ?1 where a.id = ?2")
    void updateAnimalTypesById(List<AnimalType> animalTypes, Long id);
    @Transactional
    @Modifying
    @Query("""
            update Animal a set a.weight = ?1, a.length = ?2, a.height = ?3, a.gender = ?4, a.lifeStatus = ?5,
             a.deathDateTime=?6, a.visitedLocations = ?7, a.account = ?8
            where a.id = ?9""")
    Animal updateAnimal(
            Float weight, Float length, Float height, String gender, String lifeStatus, Date deathDateTime,
            List<VisitedLocation> visitedLocation, Account account, Long id);

    Optional<Animal> findByAnimalTypesId(Long id);
}
