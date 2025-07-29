package com.groupware.exception;

import com.groupware.common.CommonResponse;
import com.groupware.common.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleAllException(Exception ex, WebRequest request) {
        return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "", ex.getLocalizedMessage());
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public CommonResponse<?> TodoException(Exception ex, WebRequest request) {
        return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "Not found", "Object does not exist");
    }
}