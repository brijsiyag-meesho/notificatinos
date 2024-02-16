package com.meesho.notification.exceptions;
public class SmsRequestNotFoundException extends RuntimeException {

    public SmsRequestNotFoundException(String message) {
        super(message);
    }
}
