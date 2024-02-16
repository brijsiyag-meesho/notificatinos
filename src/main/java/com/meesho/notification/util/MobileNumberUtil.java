package com.meesho.notification.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileNumberUtil {
    private static final String INDIAN_MOBILE_REGEX = "(?:\\+91|0)?([6789]\\d{9})";
    private static final String INDIAN_MOBILE_CODE = "+91";
    public static String formatIndianMobileNumber(String inputNumber) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile(INDIAN_MOBILE_REGEX);
        Matcher matcher = pattern.matcher(inputNumber);
        if (matcher.matches()) {
            return INDIAN_MOBILE_CODE + matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid Indian mobile number pattern: " + inputNumber);
    }
}
