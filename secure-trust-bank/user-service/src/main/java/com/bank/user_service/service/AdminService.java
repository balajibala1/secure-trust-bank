package com.bank.user_service.service;

import com.bank.user_service.dto.AccountDto;
import com.bank.user_service.dto.ProspectApplicationDto;
import com.bank.user_service.exception.ApplicationNumberNotFoundException;

import java.util.List;

public interface AdminService {
    List<ProspectApplicationDto> getAllPendingApplications(String type);
    String approvePendingCreditCardApplication(String applicationNumber) throws ApplicationNumberNotFoundException;
    String rejectPendingApplication(String applicationNumber) throws ApplicationNumberNotFoundException;
    AccountDto approvePendingOnlineBankingApplication(String applicationNumber) throws ApplicationNumberNotFoundException;

}
