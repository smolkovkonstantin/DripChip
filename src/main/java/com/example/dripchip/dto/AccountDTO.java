package com.example.dripchip.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

public class AccountDTO {

    protected interface Id {
        @Positive Integer getId();
    }

    protected interface FirstName {
        String getFirstName();
    }

    protected interface LastName {
        String getLastName();
    }

    protected interface Email {
        String getEmail();
    }

    protected interface Password {
        String getPassword();
    }


    protected interface From {
        @Min(0) Integer getFrom();
    }

    protected interface Size {
        @Min(1) Integer getSize();
    }


    public enum Request {
        ;

        @Getter
        public static class UpdateAccount implements FirstName, LastName, Email, Password {
            @NotBlank
            @NotNull
            private String firstName;
            @NotBlank
            @NotNull
            private String lastName;
            @NotBlank
            @NotNull
            @jakarta.validation.constraints.Email
            private String email;
            @NotBlank
            @NotNull
            private String password;
        }

        @Getter
        @Setter
        @Builder
        public static class SearchAccount implements FirstName, LastName, Email, From, Size {
            private String firstName;
            private String lastName;
            private String email;
            private Integer from;
            private Integer size;
        }
    }

    public enum Response {
        ;

        @Getter
        @Setter
        @Builder
        public static class Information implements Id, FirstName, LastName, Email {
            Integer id;
            String firstName;
            String lastName;
            String email;
        }

        @Getter
        @Setter
        @Builder
        public static class UpdateAccount implements Id, FirstName, LastName, Email {
            private Integer id;
            private String firstName;
            private String lastName;
            private String email;
        }

        public static class Empty {
        }
    }

}
