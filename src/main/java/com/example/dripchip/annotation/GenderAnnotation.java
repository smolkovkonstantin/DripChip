package com.example.dripchip.annotation;

import com.example.dripchip.annotation.validator.GenderValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Target({FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderValidator.class)
@Documented
public @interface GenderAnnotation {
    String message() default "Gender may be MALE, FEMALE or OTHER";

    Class<? extends com.example.dripchip.entites.model.Gender>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
