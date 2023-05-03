package com.example.dripchip.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VisitedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Animal> animal;

    private Date dateTimeOfVisitLocationPoint;

    @ManyToOne
    @JoinColumn(name = "location_point_id")
    private LocationPoint locationPoint;
}
