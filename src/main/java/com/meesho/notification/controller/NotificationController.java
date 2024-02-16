package com.meesho.notification.controller;

import com.meesho.notification.exceptions.InvalidRequstBodyException;
import com.meesho.notification.exceptions.SmsRequestNotFoundException;
import com.meesho.notification.models.dto.BlackListNumbersDTO;
import com.meesho.notification.models.dto.SmsRequestDTO;
import com.meesho.notification.models.entities.BlacklistedMobileNumber;
import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.models.entities.elasticsearch.ElasticSearchSmsRequest;
import com.meesho.notification.models.response.BaseResponse;
import com.meesho.notification.models.response.PageableBaseResponse;
import com.meesho.notification.producers.SmsEventsProducer;
import com.meesho.notification.service.sms.BlacklistService;
import com.meesho.notification.service.sms.SmsService;
import com.meesho.notification.util.MobileNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.meesho.notification.util.constants.GlobalConstants.ApiEndpoints.*;


// TODO: Handle Individual Exceptions instead of Generic Exception Class


@RestController
@Slf4j
public class NotificationController {
    SmsService smsService;
    BlacklistService blacklistService;
    SmsEventsProducer smsEventsProducer;

    @Autowired
    public NotificationController(SmsEventsProducer smsEventsProducer, SmsService smsService, BlacklistService blacklistService) {
        this.smsEventsProducer = smsEventsProducer;
        this.smsService = smsService;
        this.blacklistService = blacklistService;
    }

    @PostMapping(SEND_SMS_API_PATH)
    public ResponseEntity<BaseResponse> sendSms(@RequestBody SmsRequestDTO smsRequestDTO) throws IllegalArgumentException {
        String phoneNumber = smsRequestDTO.getPhoneNumber();
        String message = smsRequestDTO.getMessage();

        if (phoneNumber == null || phoneNumber.isBlank()){
            throw new InvalidRequstBodyException("Phone Number should not be empty.");
        }
        if(message == null || message.isBlank()){
            throw new InvalidRequstBodyException("Message should not be empty.");
        }

        phoneNumber = MobileNumberUtil.formatIndianMobileNumber(phoneNumber);

        SmsRequest smsRequest = new SmsRequest(phoneNumber, message);

        smsRequest = smsService.saveSmsRequest(smsRequest);
        smsEventsProducer.sendSmsEvent(smsRequest);
        return BaseResponse.getSuccessResponse(smsRequest);
    }

    @GetMapping(GET_SMS_API_PATH)
    public ResponseEntity<BaseResponse> getSms(@PathVariable Long smsId) throws SmsRequestNotFoundException {
        Optional<SmsRequest> smsRequest = smsService.getSmsRequest(smsId);
        if(smsRequest.isEmpty()){
            throw new SmsRequestNotFoundException(String.format("No Sms found associated with id: %d", smsId));
        }
        return BaseResponse.getSuccessResponse(smsRequest.get());
    }

    @PostMapping(SMS_BLACKLIST_API_PATH)
    public ResponseEntity<BaseResponse> addToBlacklist(@RequestBody BlackListNumbersDTO blackListNumbersDTO) throws IllegalArgumentException {
        blackListNumbersDTO.setPhoneNumbers(
                blackListNumbersDTO.getPhoneNumbers().stream()
                        .map(MobileNumberUtil::formatIndianMobileNumber)
                        .collect(Collectors.toList())
        );
        ArrayList<BlacklistedMobileNumber> response = new ArrayList<>();
        for (String phoneNumber : blackListNumbersDTO.getPhoneNumbers()) {
            BlacklistedMobileNumber blacklistedMobileNumber = new BlacklistedMobileNumber(phoneNumber);
            response.add(blacklistService.addToBlacklist(blacklistedMobileNumber));
        }
        return BaseResponse.getSuccessResponse(response);
    }

    @GetMapping(SMS_BLACKLIST_API_PATH)
    public ResponseEntity<BaseResponse> getAllBlacklistedNumbers(){
        return BaseResponse.getSuccessResponse(blacklistService.getAllBlacklistedMobileNumbers());
    }

    @DeleteMapping(SMS_BLACKLIST_API_PATH)
    public ResponseEntity<BaseResponse> removeBlacklistedNumbers(@RequestBody BlackListNumbersDTO blackListNumbersDTO){
        blackListNumbersDTO.setPhoneNumbers(
                blackListNumbersDTO.getPhoneNumbers().stream()
                        .map(MobileNumberUtil::formatIndianMobileNumber)
                        .collect(Collectors.toList())
        );
        for(String phoneNumber: blackListNumbersDTO.getPhoneNumbers()) {
            blacklistService.removeBlacklistNumber(phoneNumber);
        }
        return BaseResponse.getSuccessResponse();
    }

    @GetMapping(SMS_API_PATH)
    public ResponseEntity<PageableBaseResponse> getAllSmsRequests(
        @RequestParam(name = "from", required = false) @DateTimeFormat() LocalDateTime from,
        @RequestParam(name = "to", required = false) @DateTimeFormat() LocalDateTime to,
        @RequestParam(name = "searchString", required = false) String searchString,
        Pageable pageable
    ) {
        SearchHits<ElasticSearchSmsRequest> searchHits = smsService.getAllSmsRequests(from, to, searchString, pageable);
        return PageableBaseResponse.getSuccessResponse(
                searchHits.getTotalHits(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                searchHits
                        .getSearchHits()
                        .stream()
                        .map(SearchHit::getContent)
                        .collect(Collectors.toList())
        );
    }
}
