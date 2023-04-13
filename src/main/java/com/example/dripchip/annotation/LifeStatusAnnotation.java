package com.example.dripchip.annotation;

import com.example.dripchip.annotation.validator.GenderValidator;
import com.example.dripchip.annotation.validator.LifeStatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Target({FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LifeStatusValidator.class)
@Documented
public @interface LifeStatusAnnotation {

    String message() default "LifeStatus may be ALIVE or DEAR";

    Class<? extends com.example.dripchip.entites.model.Gender>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
