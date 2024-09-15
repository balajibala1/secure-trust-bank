package com.bank.credit_card_service.repository;


import com.bank.credit_card_service.model.ProspectApplication;
import org.springframework.data.repository.CrudRepository;

public interface ProspectRepository extends CrudRepository<ProspectApplication,Integer> {
    // ProspectApplication findByUserAndTypeAndStatus(String userId, String s, String rejected);

    ProspectApplication findByUserAndType(String userId, String s);
}