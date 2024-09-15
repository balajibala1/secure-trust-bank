package com.bank.account_service.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountTypeValidator implements ConstraintValidator<ValidateAccountType, String> {
    @Override
    public void initialize(ValidateAccountType constraintAnnotation) {
    }


    @Override
    public boolean isValid(String accountType, ConstraintValidatorContext context) {
        if (accountType == null) {
            return true;
        }
        return "savings".equalsIgnoreCase(accountType) || "current".equalsIgnoreCase(accountType) || "salary".equalsIgnoreCase(accountType);
    }
}
