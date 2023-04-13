package com.example.dripchip.annotation.validator;

import com.example.dripchip.annotation.LifeStatusAnnotation;
import com.example.dripchip.entites.model.LifeStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LifeStatusValidator implements ConstraintValidator<LifeStatusAnnotation, String> {
    @Override
    public void initialize(LifeStatusAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return LifeStatus.isLifeStatus(value);
    }
}
