package com.bank.account_service.repository;

import com.bank.account_service.model.ProspectApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProspectRepository extends CrudRepository<ProspectApplication,Integer> {
    //ProspectApplication findByUserAndTypeAndStatus(String userId, String s, String rejected);

    List<ProspectApplication> findByUserAndType(String userId, String s);

    ProspectApplication findByUserAndTypeAndAccountType(String userId, String s, String accountType);

    // ProspectApplication findByUserAndTypeAndAccountType(String userId, String s, String accountType);
}
