package com.bank.user_service.service;

import com.bank.user_service.dto.GetUserDetailDto;
import com.bank.user_service.dto.UserDto;
import com.bank.user_service.dto.UserOutputDto;
import com.bank.user_service.exception.UserNotFoundException;
import com.bank.user_service.model.User;

import java.util.List;

public interface UserService {
    String registerUser(UserDto userDto);
    User getUserDetailsById(GetUserDetailDto getUserDetailDto) throws UserNotFoundException;
    void initAdmin();

    List<UserOutputDto> getAllUserDetails();
    User getUserDetailsById(Integer userId) ;
}