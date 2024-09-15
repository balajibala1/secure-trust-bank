package com.bank.auth_service.configuration;

import com.bank.auth_service.model.User;
import com.bank.auth_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        User user = userRepository.findByEmailId(emailId);
        if(user==null){
            log.error("User not found for the id :"+emailId);
            throw new UsernameNotFoundException("Invalid UserEmail or User Not Found ");
        }
        return new CustomUserDetail(user);
    }

}