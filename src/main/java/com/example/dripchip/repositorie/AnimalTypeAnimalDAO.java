package com.example.dripchip.repositorie;

import com.example.dripchip.entites.AnimalTypeAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalTypeAnimalDAO extends JpaRepository<AnimalTypeAnimal, Long> {
    List<AnimalTypeAnimal> findByAnimal_Id(Long id);
    Optional<AnimalTypeAnimal> findByAnimal_IdAndAnimalType_Id(Long id, Long id1);

}
