package com.bank.user_service.validation;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class ApplicationNumberGenerator implements IdentifierGenerator {
    private static final String prefix = "APP";
    private static final Integer INITIAL_VALUE = 1;
    private static final Integer MAX_VALUE = 10000;
    private static final Random counter = new Random();
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        int randomValue = counter.nextInt(MAX_VALUE-INITIAL_VALUE)+MAX_VALUE;
        String generateNumber =String.format("%s%05d",prefix,randomValue);
        return generateNumber;
    }
}

