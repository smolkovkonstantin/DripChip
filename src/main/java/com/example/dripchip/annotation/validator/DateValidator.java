package com.example.dripchip.annotation.validator;

import com.example.dripchip.annotation.DateAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<DateAnnotation, String> {
    @Override
    public void initialize(DateAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try {
            if (value == null) {
                return true;
            }
            formatter.parse(value);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
