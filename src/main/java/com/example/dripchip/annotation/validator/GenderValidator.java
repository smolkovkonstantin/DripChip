package com.example.dripchip.annotation.validator;

import com.example.dripchip.annotation.GenderAnnotation;
import com.example.dripchip.entites.model.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<GenderAnnotation, String> {
    @Override
    public void initialize(GenderAnnotation genderAnnotation) {
        ConstraintValidator.super.initialize(genderAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Gender.isGender(s);
    }
}
