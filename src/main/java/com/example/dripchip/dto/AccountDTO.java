package com.example.dripchip.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class AccountDTO {

    protected interface Id {
        @Positive @NotNull Integer getId();
    }

    protected interface FirstName {
        @NotBlank
        @NotNull
        String getFirstName();
    }

    protected interface LastName {
        @NotBlank
        @NotNull
        String getLastName();
    }

    protected interface Email {
        @NotBlank
        @NotNull
        @jakarta.validation.constraints.Email
        String getEmail();
    }

    protected interface Password {
        @NotBlank
        @NotNull
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
            private String firstName;
            private String lastName;
            private String email;
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
    }

}
