package com.example.dripchip.entites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "animal")
    @ToString.Exclude
    private List<AnimalTypeAnimal> animalTypeAnimals;

    @OneToMany(mappedBy = "animal")
    @OrderBy("id ASC")
    @ToString.Exclude
    private List<VisitedLocation> visitedLocations;

    @ManyToOne
    @JoinColumn(name = "account")
    private Account account;

    private Float weight;
    private Float length;
    private Float height;
    private String gender;
    private String lifeStatus;
    private Date chippingDateTime;

    @ManyToOne
    @JoinColumn(name = "chipping_location_id")
    private LocationPoint chippingLocation;

    @Nullable
    private Date deathDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Animal animal = (Animal) o;
        return getId() != null && Objects.equals(getId(), animal.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
