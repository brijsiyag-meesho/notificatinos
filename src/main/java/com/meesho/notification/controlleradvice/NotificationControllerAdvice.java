package com.meesho.notification.controlleradvice;

import com.meesho.notification.exceptions.SmsRequestNotFoundException;
import com.meesho.notification.models.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class NotificationControllerAdvice{

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return BaseResponse.getFailureResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return BaseResponse.getFailureResponse("Request body not found.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SmsRequestNotFoundException.class)
    public ResponseEntity<BaseResponse> handleSmsRequestNotFoundException(SmsRequestNotFoundException ex) {
        return BaseResponse.getFailureResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGenericException(Exception ex){
        log.error("Unhandled Exception : {}", ex.toString());
        return BaseResponse.getFailureResponse("Error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
