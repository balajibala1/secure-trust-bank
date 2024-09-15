package com.bank.user_service.service.impl;

import com.bank.user_service.dto.AccountDto;
import com.bank.user_service.dto.FeignDto;
import com.bank.user_service.dto.GetUserDetailDto;
import com.bank.user_service.dto.ProspectApplicationDto;
import com.bank.user_service.exception.ApplicationNumberNotFoundException;
import com.bank.user_service.feign.CreditCardFeign;
import com.bank.user_service.feign.FeignService;
import com.bank.user_service.model.ProspectApplication;
import com.bank.user_service.model.User;
import com.bank.user_service.repository.ProspectRepository;
import com.bank.user_service.repository.UserRepository;
import com.bank.user_service.service.AdminService;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    @NonNull
    private final CreditCardFeign creditCardFeign;
    @NonNull
    private final ProspectRepository prospectRepository;
    @NotNull
    private final UserRepository userRepository;
    @NonNull
    private final FeignService feignService;
    @Value("${approved}")
    private String APPROVED;
    @Value("${rejected}")
    private String REJECTED;
    @Value("${pending}")
    private String PENDING;


    @Override
    public List<ProspectApplicationDto> getAllPendingApplications(String type) {
        List<ProspectApplication> prospectApplications = prospectRepository.findAllByStatus(PENDING);
        List<ProspectApplicationDto> prospectApplicationDtos=new ArrayList<>();
        prospectApplications.forEach( prospectApplication -> {
            User user=userRepository.findById(Integer.parseInt(prospectApplication.getUser())).get();
            ProspectApplicationDto prospectApplicationDto=new ProspectApplicationDto();
            prospectApplicationDto.setApplicationNumber(prospectApplication.getApplicationNumber());
            prospectApplicationDto.setType(prospectApplication.getType());
            prospectApplicationDto.setUserId(user.getUserId().toString());
            prospectApplicationDto.setCity(user.getCity());
            prospectApplicationDto.setFirstName(user.getFirstName());
            prospectApplicationDto.setLastName(user.getLastName());
            prospectApplicationDto.setPanNumber(user.getPanNumber());
            prospectApplicationDtos.add(prospectApplicationDto);
        });
        List<ProspectApplicationDto> endProspectApplicationList = new ArrayList<>();
        endProspectApplicationList=prospectApplicationDtos.stream().filter( prospectApplication -> prospectApplication.getType().equals(type)).toList();
        log.info("Fetched SuccessFully");
        return endProspectApplicationList;
    }

    public String approvePendingCreditCardApplication(String applicationNumber) throws ApplicationNumberNotFoundException {
        ProspectApplication prospectApplication = prospectRepository.findByApplicationNumberAndStatusAndType(applicationNumber,
                PENDING,"Credit-Card");
        String type;
        if(prospectApplication!=null){
            log.info(prospectApplication.getUser());
            prospectApplication.setStatus(APPROVED);
            prospectApplication.setModifiedName("admin");
            prospectApplication.setModifiedDate(LocalDate.now());
            FeignDto feignDto = new FeignDto(applicationNumber,prospectApplication.getUser());
            type = generateCreditCardNumber(feignDto);
            prospectRepository.save(prospectApplication);
            return type;
        }
        else {
            throw new ApplicationNumberNotFoundException("Application Number Not Found");
        }
    }
    public String rejectPendingApplication(String applicationNumber) throws ApplicationNumberNotFoundException {
        ProspectApplication prospectApplication = prospectRepository.findByApplicationNumberAndStatus(applicationNumber,
                PENDING);
        if(prospectApplication==null){
            log.error("Application Number Not Found");
            throw new ApplicationNumberNotFoundException("Application Number Not Found");
        }
        prospectApplication.setStatus(REJECTED);
        prospectApplication.setModifiedName("Admin");
        prospectApplication.setModifiedDate(LocalDate.now());
        prospectRepository.save(prospectApplication);
        log.info("Rejected SuccessFully");
        return "Rejected Successfully "+applicationNumber;
    }

    String generateCreditCardNumber(FeignDto feignDto){
        String cardNumber = creditCardFeign.approvePendingApplication(feignDto);
        return cardNumber;
    }
    AccountDto generateAccountNumber(GetUserDetailDto userId){
        AccountDto accountDto = feignService.approvePendingAccount(userId);
        return accountDto;
    }
    public AccountDto approvePendingOnlineBankingApplication(String applicationNumber) throws ApplicationNumberNotFoundException {
        String type= null;
        ProspectApplication prospectApplication = prospectRepository.findByApplicationNumberAndStatusAndType(applicationNumber,
                PENDING,"Online-Banking");
        if (prospectApplication!=null)
        {
            String userId = prospectApplication.getUser();
            prospectApplication.setStatus(APPROVED);
            prospectApplication.setModifiedName("admin");
            prospectApplication.setModifiedDate(LocalDate.now());
            GetUserDetailDto getUserDetailDto = new GetUserDetailDto().builder().userId(Integer.parseInt(userId)).accountType(prospectApplication.getType()).build();
            AccountDto accountDto = generateAccountNumber(getUserDetailDto);
            prospectRepository.save(prospectApplication);
            return accountDto;
        }else {
            throw new ApplicationNumberNotFoundException("Application Number Not Found");
        }
    }

}
