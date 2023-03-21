package com.example.dripchip.repositories;

import com.example.dripchip.entites.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalDAO extends JpaRepository<Animal, Long> {

    Optional<Animal> findByAnimalsTypeId(Long id);
}
