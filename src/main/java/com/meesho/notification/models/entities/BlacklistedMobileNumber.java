package com.meesho.notification.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "blacklist_mobile_numbers_v1")
@Entity
@Data
public class BlacklistedMobileNumber {
    @Id
    @Column(name="phone_number")
    private String phoneNumber;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public BlacklistedMobileNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BlacklistedMobileNumber() {

    }
}
