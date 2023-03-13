package com.example.dripchip.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

public class AccountDTO {

    protected interface Id {
        @Positive Integer getId();
    }

    protected interface FirstName {
        @NotBlank String getFirstName();
    }

    protected interface LastName {
        @NotBlank String getLastName();
    }

    protected interface Email {
        @NotBlank @jakarta.validation.constraints.Email String getEmail();
    }

    protected interface Password {
        @NotBlank String getPassword();
    }


    public enum Response {
        ;

        @Value
        @Builder
        public static class Information implements Id, FirstName, LastName, Email{
            Integer id;
            String firstName;
            String lastName;
            String email;
        }
    }

}
