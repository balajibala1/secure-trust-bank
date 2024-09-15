package com.bank.user_service.controller;

import com.bank.user_service.dto.ApiResponse;
import com.bank.user_service.dto.GetUserDetailDto;
import com.bank.user_service.dto.UserDto;
import com.bank.user_service.dto.UserOutputDto;
import com.bank.user_service.exception.UserNotFoundException;
import com.bank.user_service.model.User;
import com.bank.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserController {
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid UserDto user) {
        log.info(user.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successHandler(userService.registerUser(user)));
    }

    @GetMapping("/user-details")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<User>> getUserDetails(@RequestBody GetUserDetailDto getUserDetailDto) throws  UserNotFoundException {
        log.info("user-details");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(userService.getUserDetailsById(getUserDetailDto)
        ));
    }

//    @PostConstruct
//    public void imit(){
//        userService.initAdmin();
//    }

    @GetMapping("/user-details/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<User>> getUserDetails(@PathVariable Integer userId)  {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(userService.getUserDetailsById(userId)
        ));
    }

    @GetMapping("/getallDetails")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserOutputDto>>> getAllUserDetails(){
        System.out.println("request occured");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(userService.getAllUserDetails()));
    }

}
