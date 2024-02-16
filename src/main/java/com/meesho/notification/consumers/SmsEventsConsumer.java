package com.meesho.notification.consumers;

import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.models.enums.SmsRequestStatusType;
import com.meesho.notification.models.requests.external.ImiConnectSmsRequest;
import com.meesho.notification.models.response.external.ImiConnectSmsResponse;
import com.meesho.notification.service.external.ImiConnectService;
import com.meesho.notification.service.sms.BlacklistService;
import com.meesho.notification.service.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.meesho.notification.util.constants.GlobalConstants.ImiConnect.SMS_SUCCESSFUL_STATUS_CODE;
import static com.meesho.notification.util.constants.GlobalConstants.KafkaConfigs.SMS_EVENT_TOPIC;

@Component
@Slf4j
public class SmsEventsConsumer {
    BlacklistService blacklistService;
    SmsService smsService;
    ImiConnectService imiConnectService;

    @Autowired
    public SmsEventsConsumer(SmsService smsService, BlacklistService blacklistService, ImiConnectService imiConnectService) {
        this.smsService = smsService;
        this.blacklistService = blacklistService;
        this.imiConnectService = imiConnectService;
    }

    @KafkaListener(topics = SMS_EVENT_TOPIC)
    public void onMessage(ConsumerRecord<Long, String> consumerRecord) {
        Long smsId = Long.parseLong(consumerRecord.value());
        log.info("Sms Request ID: {} received in Kafka Consumer.", smsId);
        SmsRequest smsRequest = smsService.getSmsRequest(smsId).orElse(null);
        if (smsRequest == null) {
            log.error("No SmsRequest found associated with the smsId: {}", smsId);
            return;
        }
        try {
            String mobileNumber = smsRequest.getPhoneNumber();
            String msg = smsRequest.getMessage();
            String correlationsId = UUID.randomUUID().toString();
            boolean isBlacklisted = blacklistService.checkBlackListedNumber(mobileNumber);
            if (isBlacklisted) {
                log.info("Unable to send message, Mobile Number : {} is blocked.", mobileNumber);
                smsRequest.setStatus(SmsRequestStatusType.FAILED);
                smsRequest.setFailureComments("Mobile Number is blocked.");
                smsService.saveSmsRequest(smsRequest);
                return;
            }

            ImiConnectSmsRequest requestBody = ImiConnectSmsRequest.builder()
                    .deliverychannel("sms")
                    .channels(ImiConnectSmsRequest.Channels.builder().sms(ImiConnectSmsRequest.SmsChannel.builder().text(msg).build()).build())
                    .destination(List.of(ImiConnectSmsRequest.Destination.builder().msisdn(List.of(mobileNumber)).correlationId(correlationsId).build()))
                    .build();
            ResponseEntity<ImiConnectSmsResponse> response = imiConnectService.sendSms(requestBody);
            ImiConnectSmsResponse data = response.getBody();
            if(data == null) {
                log.error("Response from IMIConnect is {} not processable for mobile number {}.", response, mobileNumber);
                handleImiConnectFailure(smsRequest, "Error at ImiConnect side.", Optional.empty());
                return;
            }
            var responseData = data.getResponse().get(0);
            var code = responseData.getCode();
            var description = responseData.getDescription();
            if (Objects.equals(code, SMS_SUCCESSFUL_STATUS_CODE)) {
                smsRequest.setStatus(SmsRequestStatusType.SUCCESS);
                smsService.saveSmsRequest(smsRequest);
                log.info("Sms sent successfully, smsId: {}", smsId);
            } else {
                handleImiConnectFailure(smsRequest, description, Optional.of(code));
                log.error("ImiConnect message not sent for the SmsRequest ID: {}, ImiConnect Response: {}", smsRequest.getId(), data);
            }
        } catch (NumberFormatException ex) {
            handleImiConnectFailure(smsRequest, ex.getMessage(), Optional.empty());
            log.error("Invalid format for SMS ID in Kafka message: {}", consumerRecord.value());
        } catch (RestClientException ex) {
            handleImiConnectFailure(smsRequest, "ImiConnect Connection failed.", Optional.empty());
            log.error("Unable to connect to ImiConnect, message: {}", ex.getMessage());
        }
        catch (Exception ex) {
            handleImiConnectFailure(smsRequest, "Something bad happened.", Optional.empty());
            log.error("Exception occurred while processing Kafka message err: {}", ex.toString());
        }
    }
    private void handleImiConnectFailure(SmsRequest smsRequest, String description, Optional<String> code) {
        smsRequest.setStatus(SmsRequestStatusType.FAILED);
        smsRequest.setFailureComments(description);
        code.ifPresent(smsRequest::setFailureCode);
        smsService.saveSmsRequest(smsRequest);
    }
}
