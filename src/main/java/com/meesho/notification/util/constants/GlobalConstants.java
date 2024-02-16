package com.meesho.notification.util.constants;

public class GlobalConstants {
    public static class ApiEndpoints {
        public static final String SEND_SMS_API_PATH = "/api/v1/sms/send";
        public static final String SMS_API_PATH = "/api/v1/sms";
        public static final String GET_SMS_API_PATH = "/api/v1/sms/{smsId}";
        public static final String SMS_BLACKLIST_API_PATH = "/api/v1/blacklist";
    }

    public static class ImiConnect {
        public static final String SMS_SUCCESSFUL_STATUS_CODE = "1001";
    }

    public static class KafkaConfigs {
        public static final String SMS_EVENT_TOPIC = "sms_events";
    }
}
