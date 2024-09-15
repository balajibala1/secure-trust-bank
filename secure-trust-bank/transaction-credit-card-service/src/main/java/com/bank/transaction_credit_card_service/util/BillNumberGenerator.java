package com.bank.transaction_credit_card_service.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class BillNumberGenerator {
    Random randomNumber = new Random();
    public Integer generateBillNumber(){
        return randomNumber.nextInt(1,10000);
    }
}