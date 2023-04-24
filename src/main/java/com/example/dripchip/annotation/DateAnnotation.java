package com.example.dripchip.annotation;


import com.example.dripchip.annotation.validator.DateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface DateAnnotation {
    String message() default "Date doesn't meet the standard ISO-8601";

    Class<? extends com.example.dripchip.entites.model.Gender>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
