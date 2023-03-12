package com.example.dripchip.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

public class RegistrationDTO {
    private interface Id {
        @Positive Integer getId();
    }

    private interface firstName {
        @NotBlank String getFirstName();
    }

    private interface lastName {
        @NotBlank String getLastName();
    }

    private interface email {
        @NotBlank @Email String getEmail();
    }

    private interface password {
        @NotBlank String getPassword();
    }

    public enum Request {
        ;

        @Getter
        @Setter
        public static class Registration implements firstName, lastName, email, password {

            String firstName;
            String lastName;
            String email;
            String password;
        }
    }

    public enum Response {
        ;

        @Value
        @Builder
        public static class Registration implements Id, firstName, lastName, email {
            Integer id;
            String firstName;
            String lastName;
            String email;
        }
    }
}

