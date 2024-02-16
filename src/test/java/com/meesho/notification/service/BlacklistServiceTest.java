package com.meesho.notification.service;

import com.meesho.notification.models.dao.BlacklistNumberDao;
import com.meesho.notification.models.entities.BlacklistedMobileNumber;
import com.meesho.notification.service.sms.BlacklistService;
import com.meesho.notification.service.sms.SmsRedisBlacklistCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlacklistServiceTest {
    @Mock
    public BlacklistNumberDao blacklistNumberDao;
    @Mock
    public SmsRedisBlacklistCacheService smsRedisBlacklistCacheService;
    @InjectMocks
    public BlacklistService blacklistService;

    @Test
    public void addToBlacklist(){
        BlacklistedMobileNumber blacklistedMobileNumber = new BlacklistedMobileNumber("+919571805234");

        Answer<BlacklistedMobileNumber> answer = invocation -> {
            BlacklistedMobileNumber input = invocation.getArgument(0);
            input.setCreatedAt(LocalDateTime.now());
            return input;
        };


        given(blacklistNumberDao.save(any(BlacklistedMobileNumber.class))).willAnswer(answer);
        BlacklistedMobileNumber output = blacklistService.addToBlacklist(blacklistedMobileNumber);

        verify(blacklistNumberDao).save(blacklistedMobileNumber);
        verify(smsRedisBlacklistCacheService).set(blacklistedMobileNumber.getPhoneNumber(), true);

        assertThat(output.getCreatedAt()).isNotNull();
    }


    @Test
    public void removeBlacklistNumber(){
        // Test Case 1: Remove a blacklisted number

        Optional<BlacklistedMobileNumber> blacklistedMobileNumber = Optional.of(new BlacklistedMobileNumber("+919571805234"));

        String mobileNumber = blacklistedMobileNumber.get().getPhoneNumber();
        when(blacklistNumberDao.findById(any(String.class))).thenReturn(blacklistedMobileNumber);

        blacklistService.removeBlacklistNumber(mobileNumber);

        verify(blacklistNumberDao).delete(blacklistedMobileNumber.get());
        verify(smsRedisBlacklistCacheService).delete(mobileNumber);

        // Test Case 2: Remove a not blacklisted number

        given(blacklistNumberDao.findById(any(String.class))).willReturn(Optional.empty());

        blacklistService.removeBlacklistNumber(mobileNumber);

        verify(blacklistNumberDao).delete(any(BlacklistedMobileNumber.class));
        verify(smsRedisBlacklistCacheService).delete(any(String.class));
    }

    @Test
    public void getAllBlacklistedMobileNumbers(){
        BlacklistedMobileNumber blacklistedMobileNumber = new BlacklistedMobileNumber("+919571805234");
        BlacklistedMobileNumber blacklistedMobileNumber1 = new BlacklistedMobileNumber("+918239195234");
        Iterable<BlacklistedMobileNumber> blacklistedMobileNumbers = List.of(blacklistedMobileNumber, blacklistedMobileNumber1);

        given(blacklistNumberDao.findAll()).willReturn(blacklistedMobileNumbers);

        Iterable<BlacklistedMobileNumber> output = blacklistService.getAllBlacklistedMobileNumbers();
        assertThat(StreamSupport.stream(output.spliterator(), false).count()).isEqualTo(2);

        verify(blacklistNumberDao).findAll();
    }

    @Test
    public void checkBlackListedNumber(){
        BlacklistedMobileNumber blacklistedMobileNumber = new BlacklistedMobileNumber("9571805234");
        String mobileNumber = blacklistedMobileNumber.getPhoneNumber();

        given(smsRedisBlacklistCacheService.get(any(String.class))).willReturn(Optional.of(true));
        Boolean isBlacklisted = blacklistService.checkBlackListedNumber(mobileNumber);
        assertThat(isBlacklisted).isTrue();
        verifyNoInteractions(blacklistNumberDao);

        given(smsRedisBlacklistCacheService.get(any(String.class))).willReturn(Optional.empty());
        given(blacklistNumberDao.findById(any(String.class))).willReturn(Optional.empty());
        isBlacklisted = blacklistService.checkBlackListedNumber(mobileNumber);
        assertThat(isBlacklisted).isFalse();
        verify(smsRedisBlacklistCacheService).set(mobileNumber, false);

        given(blacklistNumberDao.findById(any(String.class))).willReturn(Optional.of(blacklistedMobileNumber));
        isBlacklisted = blacklistService.checkBlackListedNumber(mobileNumber);
        assertThat(isBlacklisted).isTrue();
        verify(smsRedisBlacklistCacheService).set(mobileNumber, true);
    }
}
