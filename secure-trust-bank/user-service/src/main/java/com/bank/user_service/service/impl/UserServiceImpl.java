package com.bank.user_service.service.impl;

import com.bank.user_service.dto.GetUserDetailDto;
import com.bank.user_service.dto.UserDto;
import com.bank.user_service.dto.UserOutputDto;
import com.bank.user_service.exception.UserNotFoundException;
import com.bank.user_service.model.User;
import com.bank.user_service.repository.UserRepository;
import com.bank.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final PasswordEncoder passwordEncoder;
    @NonNull
    private final ModelMapper modelMapper;
    public String registerUser(@Valid UserDto userDto)  {
        User user = modelMapper.map(userDto,User.class);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedName(user.getEmailId());
        user.setCreatedDate(LocalDate.now());
        userRepository.save(user);
        return "User Added";
    }

    public void initAdmin(){
        User user = new User();
        user.setLastName("ADMIN");
        user.setPassword(passwordEncoder.encode("Admin"));
        user.setEmailId("admin@gmail.com");
        user.setAddress("Chennai");
        user.setRole("ADMIN");
        user.setPhoneNumber("1234567890");
        user.setPanNumber("ADMIN1234A");
        user.setDateOfBirth("12/10/2000");
        user.setGender("Male");
        user.setCity("Chennai");
        user.setStreet("adstreet");
        user.setPinCode("123456");
        user.setCreatedDate(LocalDate.now());
        user.setCreatedName("Admin");
        userRepository.save(user);
    }

    public List<UserOutputDto> getAllUserDetails() {
        List<User> result= userRepository.findAll();
        List<UserOutputDto> userDetails=new ArrayList<>();
        result.forEach((user)->{
            if(user.getRole().equals("USER")) {
                UserOutputDto newUser = new UserOutputDto();
                newUser.setUserId(user.getUserId());
                newUser.setGender(user.getGender());
                newUser.setCity(user.getCity());
                newUser.setAddress(user.getAddress());
                newUser.setEmailId(user.getEmailId());
                newUser.setPinCode(user.getPinCode());
                newUser.setStreet(user.getStreet());
                newUser.setLastName(user.getLastName());
                newUser.setPanNumber(user.getPanNumber());
                newUser.setDateOfBirth(user.getDateOfBirth());
                newUser.setFirstName(user.getFirstName());
                newUser.setPhoneNumber(user.getPhoneNumber());
                userDetails.add(newUser);
            }
        });
        return userDetails;
    }

    @Override
    public User getUserDetailsById(GetUserDetailDto getUserDetailDto) {
        return userRepository.findById(getUserDetailDto.getUserId()).get();
    }
    @Override
    public User getUserDetailsById(Integer userId){
        return userRepository.findById(userId).get();
    }
}
