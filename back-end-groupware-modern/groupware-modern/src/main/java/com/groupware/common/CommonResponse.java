package com.groupware.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CommonResponse<D> {
    private HttpStatus status;

    private ResponseCode code;

    private String message;

    private D result;

    public static <D> CommonResponse<D> of(HttpStatus status, ResponseCode code, String message, D result) {
        CommonResponse<D> ins = new CommonResponse<D>();
        ins.setStatus(status);
        ins.setCode(code);
        ins.setMessage(message);
        ins.setResult(result);
        return ins;
    }

    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", data);
    }
}
