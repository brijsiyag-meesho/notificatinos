package com.meesho.notification.models.requests.external;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ImiConnectSmsRequest {

    private String deliverychannel;
    private Channels channels;
    private List<Destination> destination;
    @Data
    @Builder
    public static class Channels {
        private SmsChannel sms;
    }
    @Data
    @Builder
    public static class SmsChannel {
        private String text;
    }
    @Data
    @Builder
    public static class Destination {
        private List<String> msisdn;
        private String correlationId;
    }

}
