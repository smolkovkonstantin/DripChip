package com.example.dripchip.entites;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double latitude;

    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @OneToMany(mappedBy = "locationPoint", fetch=FetchType.LAZY)
    @ToString.Exclude
    private List<VisitedLocation> visitedLocations;
}
