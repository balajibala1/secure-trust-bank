package com.bank.credit_card_service.repository;

import com.bank.credit_card_service.model.CreditCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CreditCardRepository extends CrudRepository<CreditCard,Integer> {
    Optional<CreditCard> findByCreditCardNumber(String creditCardNumber);

    CreditCard findByUserId(String substring);
}