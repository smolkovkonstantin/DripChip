package com.example.dripchip.entites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;

@Entity
@Data
@ToString
public class VisitedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date dateTimeOfVisitLocationPoint;

    @ManyToOne
    @JoinColumn(name = "location_point_id")
    private Location locationPoint;
}
