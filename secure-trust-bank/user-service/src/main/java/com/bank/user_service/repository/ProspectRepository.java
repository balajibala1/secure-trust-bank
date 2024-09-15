package com.bank.user_service.repository;

import com.bank.user_service.model.ProspectApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProspectRepository extends CrudRepository<ProspectApplication,Integer> {
    List<ProspectApplication> findAllByStatus(String pending);
    ProspectApplication findByApplicationNumberAndStatus(String applicationNumber,String status);

    List<ProspectApplication> findAllByStatusAndType(String pending, String s);

    ProspectApplication findByApplicationNumberAndStatusAndType(String applicationNumber, String pending, String s);

    ProspectApplication findByApplicationNumberAndType(String applicationNumber, String s);
}
