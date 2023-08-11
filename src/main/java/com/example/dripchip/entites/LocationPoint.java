package com.example.dripchip.entites;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "locationPoint", fetch=FetchType.LAZY)
    @OrderBy("id ASC")
    @ToString.Exclude
    private List<VisitedLocation> visitedLocations;

    @OneToMany(mappedBy = "chippingLocation", fetch=FetchType.LAZY)
    @ToString.Exclude
    private List<Animal> animals;
}
