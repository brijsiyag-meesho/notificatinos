package com.meesho.notification.controller;

import com.meesho.notification.models.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
public class UtilController {
    @GetMapping("/health")
    public ResponseEntity<BaseResponse> healthCheck() {
        return BaseResponse.getSuccessResponse("Ok");
    }
}
