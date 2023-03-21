package com.example.dripchip.dto;

import com.example.dripchip.entites.model.Genger;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class AnimalsDTO {
    public interface AnimalTypes {
        @NotNull @Size(min = 1)
        Long[] getAnimalTypes();
    }

    public interface Weight {
        @NotNull @Min(1)
        Float getWeight();
    }

    public interface Length {
        @NotNull @Min(1)
        Float getLength();
    }

    public interface Height {
        @NotNull @Min(1)
        Float getHeight();
    }

    public interface Gender {
        @NotNull
        Genger getGender();
    }

    public interface ChipperId {
        @NotNull @Min(1) Integer getChipperId();
    }

    public interface ChippingLocationId {
        Long getChippingLocationId();
    }

    public interface VisitedLocation {
        Long[] getVisitedLocation();
    }

    public interface DeathDateTime {
        Date getDeathDateTime();
    }

    

}
