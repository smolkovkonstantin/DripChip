package com.example.dripchip.entites;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@Entity
public class AnimalsTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String type;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
