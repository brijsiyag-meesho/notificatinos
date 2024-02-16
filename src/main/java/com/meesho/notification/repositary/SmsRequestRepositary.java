package com.meesho.notification.repositary;

import com.meesho.notification.models.entities.SmsRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRequestRepositary extends CrudRepository<SmsRequest, Long> {

}
