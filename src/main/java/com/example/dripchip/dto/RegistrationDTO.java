package com.example.dripchip.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

public class RegistrationDTO {


    public enum Request {
        ;

        @Getter
        @Setter
        public static class Registration implements AccountDTO.FirstName, AccountDTO.LastName, AccountDTO.Email, AccountDTO.Password {

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
        public static class Registration implements AccountDTO.Id, AccountDTO.FirstName, AccountDTO.LastName, AccountDTO.Email {
            Integer id;
            String firstName;
            String lastName;
            String email;
        }
    }
}

