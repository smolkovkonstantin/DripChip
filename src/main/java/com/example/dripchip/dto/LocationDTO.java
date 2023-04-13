package com.example.dripchip.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class LocationDTO {

    protected interface Id {
        Long getId();
    }

    private interface Latitude {
        @Max(90) @Min(-90) @NotNull Double getLatitude();
    }

    private interface Longitude {
        @Max(180) @Min(-180) @NotNull Double getLongitude();
    }

    public enum Request {
        ;
        @Getter
        @Setter
        public static class Location implements Longitude, Latitude {
            private Double longitude;
            private Double latitude;
        }
    }

    public enum Response {
        ;

        @Getter
        @Setter
        @Builder
        public static class Location implements Id, Longitude, Latitude {
            private Long id;
            private Double longitude;
            private Double latitude;
        }

        @Builder
        public static class Empty {
        }
    }

}
