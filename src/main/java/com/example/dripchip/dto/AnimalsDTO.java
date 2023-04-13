package com.example.dripchip.dto;

import com.example.dripchip.annotation.GenderAnnotation;
import com.example.dripchip.annotation.LifeStatusAnnotation;
import com.example.dripchip.entites.model.LifeStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class AnimalsDTO {
    private interface AnimalTypes {
        @NotNull @Size(min = 1)
        Long[] getAnimalTypes();
    }

    private interface Weight {
        @NotNull @Min(1)
        Float getWeight();
    }

    private interface Length {
        @NotNull @Min(1)
        Float getLength();
    }

    private interface Height {
        @NotNull @Min(1)
        Float getHeight();
    }

    private interface Gender {
        @NotNull
        @GenderAnnotation
        String getGender();
    }

    private interface ChipperId {
        @NotNull @Min(1) Integer getChipperId();
    }

    private interface ChippingLocationId {
        Long getChippingLocationId();
    }

    private interface VisitedLocation {
        long[] getVisitedLocation();
    }

    private interface DeathDateTime {
        Date getDeathDateTime();
    }

    private interface LifeStatus {

        @NotNull
        @LifeStatusAnnotation
        String getLifeStatus();
    }

    private interface ChippingDateTime {
        Date getChippingDateTime();
    }


    public enum Request {
        ;

        @Getter
        public static class Registration implements AnimalTypes, Weight, AnimalsDTO.Gender, ChipperId, ChippingLocationId, Length, Height {
            private Long[] animalTypes;
            private Float weight;
            private Float length;
            private Float height;
            private String gender;
            private Integer chipperId;
            private Long chippingLocationId;
        }

        @Getter
        public static class Update implements Weight, Length, Height, Gender, AnimalsDTO.LifeStatus, ChipperId, ChippingLocationId {
            private Float weight;
            private Float length;
            private Float height;
            private String gender;
            private String lifeStatus;
            private Integer chipperId;
            private Long chippingLocationId;
        }
    }

    public enum Response {
        ;

        @Getter
        @Setter
        @Builder
        public static class Information implements LocationDTO.Id, AnimalTypes, Weight, Length, Height, Gender, AnimalsDTO.LifeStatus, ChippingDateTime, ChipperId, ChippingLocationId, VisitedLocation, DeathDateTime {
            private Long id;
            private Long[] animalTypes;
            private Float weight;
            private Float length;
            private Float height;
            private String gender;
            private Integer chipperId;
            private Long chippingLocationId;
            private long[] visitedLocation;
            private Date deathDateTime;
            private String lifeStatus;
            private Date chippingDateTime;
        }
    }
}
