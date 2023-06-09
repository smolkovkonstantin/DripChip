package com.example.dripchip.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

public class RegistrationDTO {


    public enum Request {
        ;

        @Getter
        @Setter
        public static class Registration implements AccountDTO.FirstName, AccountDTO.LastName, AccountDTO.Email, AccountDTO.Password {

            private String firstName;
            private String lastName;
            private String email;
            private String password;
        }
    }
}

