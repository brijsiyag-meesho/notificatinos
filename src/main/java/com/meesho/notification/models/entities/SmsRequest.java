package com.meesho.notification.models.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meesho.notification.models.entities.elasticsearch.ElasticSearchSmsRequest;
import com.meesho.notification.models.enums.SmsRequestStatusType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="sms_requests_v1")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private SmsRequestStatusType status = SmsRequestStatusType.PENDING;

    @Column(name = "failure_code", length = 50)
    private String failureCode;

    @Column(name = "failure_comments", columnDefinition = "TEXT")
    private String failureComments;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;


    public SmsRequest(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public SmsRequest() {

    }

    public ElasticSearchSmsRequest toElasticsearchSmsRequest() {
        ElasticSearchSmsRequest elasticsearchSmsRequest = new ElasticSearchSmsRequest();
        elasticsearchSmsRequest.setId(id);
        elasticsearchSmsRequest.setPhoneNumber(phoneNumber);
        elasticsearchSmsRequest.setMessage(message);
        elasticsearchSmsRequest.setFailureCode(failureCode);
        elasticsearchSmsRequest.setFailureComment(failureComments);
        elasticsearchSmsRequest.setStatus(status);
        elasticsearchSmsRequest.setCreatedAt(createdAt);
        elasticsearchSmsRequest.setUpdatedAt(createdAt);
        return elasticsearchSmsRequest;
    }
}
