package com.example.dripchip.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class AccountDTO {

    protected interface Id {
        @Positive Integer getId();
    }

    protected interface FirstName {
        @NotBlank @NotNull String getFirstName();
    }

    protected interface LastName {
        @NotBlank @NotNull String getLastName();
    }

    protected interface Email {
        @NotBlank @NotNull @jakarta.validation.constraints.Email String getEmail();
    }

    protected interface Password {
        @NotBlank @NotNull String getPassword();
    }


    public enum Request {
        ;

        @Getter
        public static class UpdateAccount implements FirstName, LastName, Email, Password {
            String firstName;
            String lastName;
            String email;
            String password;
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
