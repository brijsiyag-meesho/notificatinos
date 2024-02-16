package com.meesho.notification.producers;

import com.meesho.notification.models.entities.SmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.meesho.notification.util.constants.GlobalConstants.KafkaConfigs.SMS_EVENT_TOPIC;

@Component
@Slf4j
public class SmsEventsProducer {
    private final KafkaTemplate<Long, String> kafkaTemplate;

    public SmsEventsProducer(KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<Long, String>> sendSmsEvent(SmsRequest smsRequest) {
        var key = smsRequest.getId();
        // Converting id to string in value because kafka was giving strange encoded text in consumer
        // TODO: Use Long as value in producer
        var value = key.toString();
        var producerRecord = buildProducerRecord(key, value);

        CompletableFuture<SendResult<Long, String>> completableFuture = kafkaTemplate.send(producerRecord);

        return completableFuture.whenComplete((sendResult, throwable) -> {
            if(throwable!=null){
                handleFailure(key, value, throwable);
            }else{
                handleSuccess(key, value, sendResult);
            }
        });
    }

    private ProducerRecord<Long, String> buildProducerRecord(Long key, String value) {

        List<Header> recordHeaders = List.of(new RecordHeader("event-source","scanner".getBytes()));

        return new ProducerRecord<Long,String>(SMS_EVENT_TOPIC,null, key, value, recordHeaders);
    }


    private void handleSuccess(Long key, String value, SendResult<Long, String> sendResult) {
        log.info("Sms event sent successfully for the key : {} and the value : {} , partition is {} ",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Long key, String value, Throwable throwable) {
        log.error("Error sending the event and the exception is {} ", throwable.getMessage(), throwable );
    }
}
