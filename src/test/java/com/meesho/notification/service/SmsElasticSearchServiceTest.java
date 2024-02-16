package com.meesho.notification.service;

import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.models.entities.elasticsearch.ElasticSearchSmsRequest;
import com.meesho.notification.repositary.elasticsearch.ElasticSearchSmsRequestRepositary;
import com.meesho.notification.service.sms.SmsElasticSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SmsElasticSearchServiceTest {
    @InjectMocks
    public SmsElasticSearchService smsElasticSearchService;
    @Mock
    public ElasticSearchSmsRequestRepositary elasticSearchSmsRequestRepositary;

    @Test
    public void saveSmsRequest(){
        SmsRequest smsRequest = new SmsRequest("+919571805234", "Hey, Birju...");

        given(elasticSearchSmsRequestRepositary.save(any(ElasticSearchSmsRequest.class))).willReturn(null);

        smsElasticSearchService.saveSmsRequest(smsRequest);

        verify(elasticSearchSmsRequestRepositary).save(smsRequest.toElasticsearchSmsRequest());
    }
}
