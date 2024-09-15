package com.bank.user_service.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String status;
    private T data;
    private Error error;

    public static <T>  ApiResponse<T> successHandler(T response){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("Success");
        apiResponse.setData(response);
        apiResponse.setError(null);
        return apiResponse;
    }
    public static <T> ApiResponse<T> failureHandler(Object error, Integer httpStatus){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("Failed");
        apiResponse.setData(null);
        Error error1 = new Error();
        error1.setErrorCode(httpStatus);
        error1.setMsg(error);
        apiResponse.setError(error1);
        return apiResponse;
    }
}

