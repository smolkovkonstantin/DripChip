package com.example.dripchip.entites;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AnimalsType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String type;

    @OneToMany(mappedBy = "animalsType", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Animal> animals;
}
