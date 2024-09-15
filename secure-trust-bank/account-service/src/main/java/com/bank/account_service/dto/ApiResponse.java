package com.bank.account_service.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;
    private T data;
    private List<Error> errors;

    public static <T> ApiResponse<T> successHandler(T msg){
        ApiResponse apiResponse = new ApiResponse("Success",msg,null);
        return apiResponse;
    }
    public static <T> ApiResponse<T> failureHandler(Object error, Integer httpStatus) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("Failed");
        apiResponse.setData(null);
        Error errorResponse = new Error();
        errorResponse.setCode(httpStatus);
        errorResponse.setMessage(error);
        List<Error> errorResponses = new ArrayList<>();
        errorResponses.add(errorResponse);
        apiResponse.setErrors(errorResponses);
        return apiResponse;
    }
}