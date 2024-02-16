package com.meesho.notification.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmsRequestDTO extends BaseDto{
    private String phoneNumber;
    private String message;
}

