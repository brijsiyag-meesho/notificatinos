package com.meesho.notification.models;

import com.meesho.notification.models.enums.CountryCode;
import lombok.Data;

// Dropping use of this class for simplicity for now.
@Data
public class MobileNumber {
    private CountryCode code = CountryCode.INDIA;
    private String mobileNumber;

    public String getMobileNumber(){
        return this.code.toString() + this.mobileNumber;
    }
}
