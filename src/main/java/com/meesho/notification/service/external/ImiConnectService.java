package com.meesho.notification.service.external;

import com.meesho.notification.models.requests.external.ImiConnectSmsRequest;
import com.meesho.notification.models.response.external.ImiConnectSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@Slf4j
public class ImiConnectService {
    RestTemplate restTemplate;

    @Value("${imiconnect.sms-send.api-url}")
    private String sendSmsUrl;

    @Value("${imiconnnect.sms.api-key}")
    private String apiKey;

    @Autowired
    public ImiConnectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ImiConnectSmsResponse> sendSms(ImiConnectSmsRequest requestBody){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("key", apiKey);

        HttpEntity<ImiConnectSmsRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(sendSmsUrl, requestEntity, ImiConnectSmsResponse.class);
    }
}
