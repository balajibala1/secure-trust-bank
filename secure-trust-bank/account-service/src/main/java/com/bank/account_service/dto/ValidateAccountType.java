package com.bank.account_service.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAccountType {
    String message() default "Invalid account type. It must be 'savings' or 'current' or 'salary' ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}