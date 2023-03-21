package com.example.dripchip.dto;

import lombok.Builder;
import lombok.Getter;

public class AnimalsTypesDTO {

    private interface Type {
        String getType();
    }

    public enum Request {
        ;

        @Getter
        public static class AnimalsTypes implements Type {
            private String type;
        }
    }

    public enum Response {
        ;

        @Getter
        @Builder
        public static class AnimalsTypes implements Type, LocationDTO.Id {
            private Long id;
            private String type;
        }

        public static class Empty {
        }
    }

}
