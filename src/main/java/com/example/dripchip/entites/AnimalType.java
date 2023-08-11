package com.example.dripchip.entites;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AnimalType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String type;

    @OneToMany(mappedBy = "animalType")
    @ToString.Exclude
    private List<AnimalTypeAnimal> animalTypeAnimals;
}
