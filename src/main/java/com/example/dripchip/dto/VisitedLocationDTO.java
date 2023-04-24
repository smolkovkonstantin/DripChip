package com.example.dripchip.dto;

import com.example.dripchip.annotation.DateAnnotation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

public class VisitedLocationDTO {

    private interface VisitedLocationPointId {
        @NotNull
        @Min(1)
        Long getVisitedLocationPointId();
    }

    private interface LocationPointId {
        @NotNull
        @Min(1)
        Long getLocationPointId();
    }

    private interface DateTimeOfVisitLocationPoint {
        Date getDateTimeOfVisitLocationPoint();
    }

    protected interface StartDateTime {
        @DateAnnotation
        String getStartDateTime();
    }

    protected interface EndDateTime {
        @DateAnnotation
        String getEndDateTime();
    }


    public enum Request {
        ;

        @Getter
        public static class Update implements VisitedLocationPointId, LocationPointId {
            private Long visitedLocationPointId;
            private Long locationPointId;
        }

        @Getter
        @Builder
        public static class Search implements StartDateTime, EndDateTime, AccountDTO.From, AccountDTO.Size {
            private String startDateTime;
            private String endDateTime;
            private Integer from;
            private Integer size;
        }
    }

    public enum Response {
        ;

        @Getter
        @Builder
        public static class VisitedLocationInfo implements LocationDTO.Id, DateTimeOfVisitLocationPoint, LocationPointId {
            private Long id;
            private Date dateTimeOfVisitLocationPoint;
            private Long locationPointId;
        }
    }
}
