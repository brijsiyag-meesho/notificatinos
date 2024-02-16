package com.meesho.notification.models.dao;

import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.repositary.SmsRequestRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SmsRequestDao {
    SmsRequestRepositary smsRequestRepositary;

    @Autowired
    public SmsRequestDao(SmsRequestRepositary smsRequestRepositary) {
        this.smsRequestRepositary = smsRequestRepositary;
    }

    public SmsRequest save(SmsRequest smsRequest) {
        return smsRequestRepositary.save(smsRequest);
    }

    public Optional<SmsRequest> findById(Long smsId){
        return smsRequestRepositary.findById(smsId);
    }
}
