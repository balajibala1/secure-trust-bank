package com.bank.credit_card_service.service;

import com.bank.credit_card_service.dto.CreditCardDto;
import com.bank.credit_card_service.exception.AlreadyAppliedException;
import com.bank.credit_card_service.exception.CustomException;
import com.bank.credit_card_service.model.CreditCard;

import java.util.Optional;

public interface CreditCardService {
    String approvePendingApplication(String applicationNumber, String userId);
    void updateAvailableLimit(CreditCard creditCard) throws CustomException;
    Optional<CreditCard> getCreditCardNumber(String cardNumber);

    String applyCreditCard(String substring) throws AlreadyAppliedException;

    CreditCardDto getAllCreditCardDetails(String substring);
}