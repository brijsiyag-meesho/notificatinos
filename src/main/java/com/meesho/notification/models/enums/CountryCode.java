package com.meesho.notification.models.enums;

import lombok.Getter;

@Getter
public enum CountryCode {
    INDIA("+91"),
    PAKISTAN("+92");
    private final String code;
    CountryCode(String code) {
        this.code = code;
    }
}