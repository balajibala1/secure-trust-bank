package com.bank.transaction_online_banking_service.service.impl;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class Validator {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static boolean isValidDateOfBirth(LocalDateTime scheduledTime) {
        if (scheduledTime == null) {
            return false;
        }

        LocalDateTime currentDate = LocalDateTime.now();
        if (scheduledTime.isBefore(currentDate)) {
            throw new IllegalArgumentException("ScheduledDate & Time cannot be in the past");
        }
        return true;
    }

    public static LocalDateTime parseDateOfBirth(String dateOfBirthStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
            return LocalDateTime.parse(dateOfBirthStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format:"+ DATE_FORMAT_PATTERN);
        }
    }
}