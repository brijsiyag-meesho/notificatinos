package com.meesho.notification.models.dto;

import lombok.Data;

import java.util.List;
@Data
public class BlackListNumbersDTO {
    private List<String> phoneNumbers;
}
