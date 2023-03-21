package com.example.dripchip.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private AnimalsType animalsType;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Location> locations;

    @ManyToOne
    @JoinColumn(name = "account")
    private Account account;

    private float weight;

    private float length;

    private float height;

    private String gender;

    private String lifeStatus;

    private Date chippingDateTime;

    private Date deathDateTime;

    private Integer chipping;
}
