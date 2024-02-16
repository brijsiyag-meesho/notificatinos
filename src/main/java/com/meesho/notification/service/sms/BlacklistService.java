package com.meesho.notification.service.sms;

import com.meesho.notification.models.dao.BlacklistNumberDao;
import com.meesho.notification.models.entities.BlacklistedMobileNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlacklistService {
    BlacklistNumberDao blacklistNumberDao;
    SmsRedisBlacklistCacheService smsRedisBlacklistCacheService;

    @Autowired
    public BlacklistService(SmsRedisBlacklistCacheService smsRedisBlacklistCacheService, BlacklistNumberDao blacklistNumberDao) {
        this.smsRedisBlacklistCacheService = smsRedisBlacklistCacheService;
        this.blacklistNumberDao = blacklistNumberDao;
    }

    public Boolean checkBlackListedNumber(String mobileNumber){
        Optional<Boolean> isMobileBlacklisted = smsRedisBlacklistCacheService.get(mobileNumber);
        if(isMobileBlacklisted.isPresent()){
            return isMobileBlacklisted.get();
        }
        isMobileBlacklisted = Optional.of(blacklistNumberDao.findById(mobileNumber).isPresent());
        smsRedisBlacklistCacheService.set(mobileNumber, isMobileBlacklisted.get());
        return isMobileBlacklisted.get();
    }

    public Iterable<BlacklistedMobileNumber> getAllBlacklistedMobileNumbers(){
        return blacklistNumberDao.findAll();
    }

    public BlacklistedMobileNumber addToBlacklist(BlacklistedMobileNumber blacklistedMobileNumber){
        smsRedisBlacklistCacheService.set(blacklistedMobileNumber.getPhoneNumber(), true);
        return blacklistNumberDao.save(blacklistedMobileNumber);
    }

    public void removeBlacklistNumber(String mobileNumber){
        Optional<BlacklistedMobileNumber> blacklistedMobileNumber = blacklistNumberDao.findById(mobileNumber);
        if (blacklistedMobileNumber.isPresent()){
            smsRedisBlacklistCacheService.delete(mobileNumber);
            blacklistNumberDao.delete(blacklistedMobileNumber.get());
        }
    }
}
