package com.bank.user_service.validation;

import com.bank.user_service.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue, String> {
    @Autowired
    private UserRepository userRepository;
    String field;
    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(userRepository.findByEmailId(value)!=null){
            return false;
        }else if (userRepository.findByPanNumber(value)!=null){
            return false;
        }else if (userRepository.findByPhoneNumber(value)!=null){
            return false;
        }
        return true;
    }
}
