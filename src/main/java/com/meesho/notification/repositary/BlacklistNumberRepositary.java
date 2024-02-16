package com.meesho.notification.repositary;

import com.meesho.notification.models.entities.BlacklistedMobileNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistNumberRepositary extends CrudRepository<BlacklistedMobileNumber, String> {

}
