package com.bank.user_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValueValidator.class)
public @interface UniqueValue{
    String message() default "Value must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();
}
