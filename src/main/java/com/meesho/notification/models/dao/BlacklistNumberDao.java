package com.meesho.notification.models.dao;

import com.meesho.notification.models.entities.BlacklistedMobileNumber;
import com.meesho.notification.repositary.BlacklistNumberRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BlacklistNumberDao {
    private final BlacklistNumberRepositary blacklistNumberRepositary;
    @Autowired
    public BlacklistNumberDao(BlacklistNumberRepositary blacklistNumberRepositary) {
        this.blacklistNumberRepositary = blacklistNumberRepositary;
    }


    public Optional<BlacklistedMobileNumber> findById(String mobileNumber) {
        return blacklistNumberRepositary.findById(mobileNumber);
    }

    public Iterable<BlacklistedMobileNumber> findAll() {
        return blacklistNumberRepositary.findAll();
    }

    public BlacklistedMobileNumber save(BlacklistedMobileNumber blacklistedMobileNumber) {
       return blacklistNumberRepositary.save(blacklistedMobileNumber);
    }

    public void delete(BlacklistedMobileNumber blacklistedMobileNumber) {
        blacklistNumberRepositary.delete(blacklistedMobileNumber);
    }
}
