package com.meesho.notification.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRequstBodyException extends IllegalArgumentException{
    final static HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

    public InvalidRequstBodyException(String s) {
        super(s);
    }

    public InvalidRequstBodyException(String message, Throwable cause) {
        super(message, cause);
    }
}
