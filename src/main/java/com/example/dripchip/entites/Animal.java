package com.example.dripchip.entites;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany
    @ToString.Exclude
    @JsonIgnore
    private List<AnimalType> animalTypes;

    @Transient
    @JsonProperty("animalTypes")
    private List<Long> animalTypesJson;

    @ManyToMany
    @JsonIgnore
    @ToString.Exclude
    private List<VisitedLocation> visitedLocations;

    @Transient
    @JsonProperty("visitedLocations")
    private List<Long> visitedLocationsJson;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account")
    private Account account;

    @Transient
    private Integer chipperId;

    @Transient
    private Long chippingLocationId;

    private Float weight;
    private Float length;
    private Float height;
    private String gender;
    private String lifeStatus;
    private Date chippingDateTime;

    @Nullable
    private Date deathDateTime;
}
