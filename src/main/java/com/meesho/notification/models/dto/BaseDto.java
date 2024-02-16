package com.meesho.notification.models.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseDto {
    public byte[] toBytesArray() {
        try {
            return new ObjectMapper().writeValueAsBytes(this);
        } catch (JsonProcessingException ex) {
            log.error("Error converting DTO to JSON string, err: {}", ex.toString());
            throw new RuntimeException("Error converting DTO to JSON string", ex);
        }
    }

}
