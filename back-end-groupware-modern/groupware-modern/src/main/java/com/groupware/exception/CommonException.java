package com.groupware.exception;

import lombok.Data;

@Data
public class CommonException extends RuntimeException{
    private String message;

    public CommonException(String message){
        super();
        this.message = message;
    }

    public static CommonException of (String message, Object... data){
        String msg = message;
        for (Object obj : data){
            if (obj == null) {
                msg = msg.replaceFirst("[{][}]", "null");
            } else {
                msg = msg.replaceFirst("[{][}]", obj.toString());
            }
        }
        return new CommonException(msg);
    }
}
