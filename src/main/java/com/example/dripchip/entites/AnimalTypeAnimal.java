package com.example.dripchip.entites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AnimalTypeAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "animal")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "animalType")
    private AnimalType animalType;
}
