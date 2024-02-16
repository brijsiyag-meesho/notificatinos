package com.meesho.notification.service.sms;

import com.meesho.notification.models.dao.SmsRequestDao;
import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.models.entities.elasticsearch.ElasticSearchSmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmsService {
    private final SmsElasticSearchService smsElasticSearchService;
    private final SmsRequestDao smsRequestDao;

    @Autowired
    public SmsService(SmsElasticSearchService smsElasticSearchService, SmsRequestDao smsRequestDao) {
        this.smsElasticSearchService = smsElasticSearchService;
        this.smsRequestDao = smsRequestDao;
    }
    public SmsRequest saveSmsRequest(SmsRequest smsRequest) {
        smsRequestDao.save(smsRequest);
        smsElasticSearchService.saveSmsRequest(smsRequest);
        return smsRequest;
    }
    public Optional<SmsRequest> getSmsRequest(Long smsId) {
        return smsRequestDao.findById(smsId);
    }

    public SearchHits<ElasticSearchSmsRequest> getAllSmsRequests(LocalDateTime from, LocalDateTime to, String searchString, Pageable pageable){
        return smsElasticSearchService.getAllSmsRequests(from, to, searchString, pageable);
    }
}

