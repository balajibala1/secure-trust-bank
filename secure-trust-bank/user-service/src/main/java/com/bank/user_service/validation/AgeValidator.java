package com.bank.user_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AgeValidator implements ConstraintValidator<ValidAge, String> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void initialize(ValidAge constraintAnnotation) {
    }

    @Override
    public boolean isValid(String dob, ConstraintValidatorContext constraintValidatorContext) {
        if (dob == null) {
            return true;
        }
        try {
            Date dob1 = dateFormat.parse(dob);
            LocalDate localDate = LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(localDate, currentDate);
            if (period.getYears() >= 18) {
                return true;
            } else {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Age must be at least 18")
                        .addConstraintViolation();
                return false;
            }
        } catch (DateTimeException | ParseException e) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Enter the Date in dd/MM/yyyy format")
                    .addConstraintViolation();
            return false;
        }

    }
}
