package com.example.dripchip.entites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "animal", fetch=FetchType.LAZY)
    @ToString.Exclude
    private List<AnimalsTypes> animalsTypes;

    @OneToMany(mappedBy = "animal", fetch=FetchType.LAZY)
    @ToString.Exclude
    private List<Location> locations;

    private float weight;

    private float length;

    private float height;

    private String gender;

    private String lifeStatus;

    private Date chippingDateTime;

    private Date deathDateTime;

    private Integer chipping;
}
