package com.bank.user_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PaymentTypeValidator implements ConstraintValidator<PaymentType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null){
            return false;
        }
        return value!=null && (value.equalsIgnoreCase("creditCard"));
    }
}
