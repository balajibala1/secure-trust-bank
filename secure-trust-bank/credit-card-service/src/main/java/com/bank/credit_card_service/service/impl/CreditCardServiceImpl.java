package com.bank.credit_card_service.service.impl;

import com.bank.credit_card_service.dto.CreditCardDto;
import com.bank.credit_card_service.exception.*;
import com.bank.credit_card_service.model.CreditCard;
import com.bank.credit_card_service.model.ProspectApplication;
import com.bank.credit_card_service.repository.*;
import com.bank.credit_card_service.service.CreditCardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import com.bank.authorization.security.config.*;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final ProspectRepository prospectRepository;
    ValidateToken jwtFilter;

    private static final String PENDING = "PENDING";

    public String approvePendingApplication(String applicationNumber, String userId){
        log.info(userId);
        Double initialLimit = 10000.0;
        CreditCard creditCard = new CreditCard();
        String creditCardNumber = generateCardNumber();
        creditCard.setCreditCardNumber(creditCardNumber);
        creditCard.setCreditCardLimit(initialLimit);
        creditCard.setAvailableLimit(initialLimit);
        creditCard.setApplicationNumber(applicationNumber);
        creditCard.setUserId(userId);
        creditCardRepository.save(creditCard);
        log.info("Credit Card Number Generated");
        return creditCardNumber;
    }

    public String generateCardNumber(){
        return "1"+ RandomStringUtils.randomNumeric(15);
    }

    public Optional<CreditCard> getCreditCardNumber(String cardNumber) {
        log.info("Credit Card Details Fetched");
        return creditCardRepository.findByCreditCardNumber(cardNumber);
    }

    public void updateAvailableLimit(CreditCard creditCard) throws CustomException {
        log.info("Credit Card Updated");
        String creditCardNumber = creditCard.getCreditCardNumber();
        CreditCard card = creditCardRepository.findByCreditCardNumber(creditCardNumber).orElseThrow(()-> new CustomException("Credit Card is not there"));
        card.setAvailableLimit(creditCard.getAvailableLimit());
        creditCardRepository.save(card);
    }
    public String applyCreditCard(String token) throws AlreadyAppliedException {
        String userId = jwtFilter.extractUserName(token);
        log.info(userId);
        ProspectApplication alreadyRegisteredApplication = prospectRepository.findByUserAndType(userId,"Credit-Card");
        if(alreadyRegisteredApplication==null) {
            ProspectApplication prospectApplication = new ProspectApplication();
            prospectApplication.setStatus(PENDING);
            prospectApplication.setCreatedName("ADMIN");
            prospectApplication.setCreatedDate(LocalDate.now());
            prospectApplication.setType("Credit-Card");
            prospectApplication.setUser(userId);
            prospectRepository.save(prospectApplication);
            return "Credit Card Applied Successfully";
        }else {
            throw new AlreadyAppliedException("The user is already applied for credit-card");
        }
    }

    @Override
    public CreditCardDto getAllCreditCardDetails(String substring) {
        log.info(substring);
        String userId = jwtFilter.extractUserName(substring);
        CreditCard card = creditCardRepository.findByUserId(userId);
        CreditCardDto creditCardDto = new CreditCardDto(card.getCreditCardNumber(),card.getAvailableLimit());
        return creditCardDto;
    }
}