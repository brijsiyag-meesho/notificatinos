package com.meesho.notification.service;

import com.meesho.notification.models.dao.SmsRequestDao;
import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.service.sms.SmsElasticSearchService;
import com.meesho.notification.service.sms.SmsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {
    @Mock
    public SmsRequestDao smsRequestDao;
    @Mock
    public SmsElasticSearchService smsElasticSearchService;
    @InjectMocks
    public SmsService smsService;

    @Test
    void saveSmsRequest(){
        SmsRequest smsRequest1 = new SmsRequest("+919571805234", "Hey, Birju...");
        Answer<SmsRequest> answer = invocation -> {
            SmsRequest input = invocation.getArgument(0);
            input.setId(1L);
            input.setCreatedAt(LocalDateTime.now());
            input.setUpdatedAt(LocalDateTime.now());
            return input;
        };

        given(smsRequestDao.save(any(SmsRequest.class))).willAnswer(answer);
        SmsRequest output = smsService.saveSmsRequest(smsRequest1);

        verify(smsRequestDao).save(smsRequest1);

        assertThat(output.getId()).isNotNull();
        assertThat(output.getCreatedAt()).isNotNull();
    }


    @Test
    void getSmsRequest(){
        SmsRequest smsRequest = new SmsRequest("+919571805234", "Hey, Birju...");
        Answer<Optional<SmsRequest>> answer = invocation -> {
            Long smsId = invocation.getArgument(0);
            if (smsId == 3) {
                return Optional.empty();
            }
            smsRequest.setId(5L);
            return Optional.of(smsRequest);
        };

        given(smsRequestDao.findById(any(Long.class))).willAnswer(answer);

        Optional<SmsRequest> output = smsService.getSmsRequest(3L);
        verify(smsRequestDao).findById(3L);
        assertThat(output.isPresent()).isEqualTo(false);

        output = smsService.getSmsRequest(5L);
        verify(smsRequestDao).findById(5L);
        assertThat(output.isPresent()).isTrue();
        assertThat(output.get().getId()).isEqualTo(5L);
    }

}
