package com.meesho.notification.models.entities.elasticsearch;

import com.meesho.notification.convertors.elasticsearch.LocalDateTimeConvertor;
import com.meesho.notification.models.enums.SmsRequestStatusType;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;

import java.time.LocalDateTime;

@Data
@Document(indexName = "sms_requests")
public class ElasticSearchSmsRequest {
    private Long id;
    private String phoneNumber;
    private String message;
    private String failureCode;
    private String failureComment;
    private SmsRequestStatusType status;

    @ValueConverter(LocalDateTimeConvertor.class)
    @Field(type=FieldType.Date)
    private LocalDateTime createdAt;

    @ValueConverter(LocalDateTimeConvertor.class)
    @Field(type=FieldType.Date)
    private LocalDateTime updatedAt;
}
