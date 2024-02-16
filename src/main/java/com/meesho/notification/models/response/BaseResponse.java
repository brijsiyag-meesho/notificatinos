package com.meesho.notification.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private int code;
    private Object data;
    private String message;

    public static <T> ResponseEntity<BaseResponse> getSuccessResponse(T data){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(data);
        baseResponse.setCode(HttpStatus.OK.value());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    public static ResponseEntity<BaseResponse> getSuccessResponse() {
        BaseResponse baseResponse = new BaseResponse();
        return new ResponseEntity<>(baseResponse, HttpStatus.NO_CONTENT);
    }

    public static <T> ResponseEntity<BaseResponse> getFailureResponse(String message, HttpStatus httpStatus){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(message);
        baseResponse.setCode(httpStatus.value());
        return new ResponseEntity<>(baseResponse, httpStatus);
    }

}
