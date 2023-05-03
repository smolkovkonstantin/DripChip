package com.example.dripchip.dto;

import com.example.dripchip.annotation.GenderAnnotation;
import com.example.dripchip.annotation.LifeStatusAnnotation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class AnimalDTO {
    private interface AnimalTypes {
        @NotNull @Size(min = 1)
        List<@NotNull @Min(1) Long> getAnimalTypes();
    }

    private interface Weight {
        @NotNull @Positive
        Float getWeight();
    }

    private interface Length {
        @NotNull @Positive
        Float getLength();
    }

    private interface Height {
        @NotNull @Positive
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

    private interface VisitedLocations {
        List<Long> getVisitedLocations();
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

    private interface OldTypeId {
        @NotNull @Min(1)
        Long getOldTypeId();
    }

    private interface NewTypeId {
        @NotNull @Min(1)
        Long getNewTypeId();
    }


    public enum Request {
        ;

        @Getter
        public static class Registration implements AnimalTypes, Weight, AnimalDTO.Gender, ChipperId, ChippingLocationId, Length, Height {
            private List<Long> animalTypes;
            private Float weight;
            private Float length;
            private Float height;
            private String gender;
            private Integer chipperId;
            private Long chippingLocationId;
        }

        @Getter
        public static class Update implements Weight, Length, Height, Gender, AnimalDTO.LifeStatus, ChipperId, ChippingLocationId {
            private Float weight;
            private Float length;
            private Float height;
            private String gender;
            private String lifeStatus;
            private Integer chipperId;
            private Long chippingLocationId;
        }

        @Getter
        public static class UpdateAnimalType implements OldTypeId, NewTypeId {
            private Long oldTypeId;
            private Long newTypeId;
        }

        @Getter
        @Builder
        public static class Search implements VisitedLocationDTO.StartDateTime, VisitedLocationDTO.EndDateTime, ChipperId, ChippingLocationId, LifeStatus, Gender, AccountDTO.From, AccountDTO.Size {
            private String startDateTime;
            private String endDateTime;
            private String gender;
            private String lifeStatus;
            private Integer chipperId;
            private Long chippingLocationId;
            private Integer from;
            private Integer size;
        }
    }

    public enum Response {
        ;

        @Getter
        @Setter
        @Builder
        public static class Information implements LocationDTO.Id, AnimalTypes, Weight, Length, Height, Gender, AnimalDTO.LifeStatus, ChippingDateTime, ChipperId, ChippingLocationId, VisitedLocations, DeathDateTime {
            private Long id;
            private List<Long> animalTypes;
            private Float weight;
            private Float length;
            private Float height;
            private String gender;
            private Integer chipperId;
            private Long chippingLocationId;
            private List<Long> visitedLocations;
            private Date deathDateTime;
            private String lifeStatus;
            private Date chippingDateTime;
        }
    }
}
