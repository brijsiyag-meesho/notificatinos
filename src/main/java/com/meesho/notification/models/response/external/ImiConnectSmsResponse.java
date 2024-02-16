package com.meesho.notification.models.response.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ImiConnectSmsResponse {
    private List<ResponseItem> response;
    @Data
    public static class ResponseItem {
        private String code;
        private String transid;
        private String description;
        @JsonIgnore
        private String correlationid;
    }
}
