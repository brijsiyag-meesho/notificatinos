package com.meesho.notification.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageableBaseResponse extends BaseResponse{
    private long totalHits;
    private int pageNumber;
    private int pageSize;
    private int totalPages;

    public static <T> ResponseEntity<PageableBaseResponse> getSuccessResponse(long totalHits, int currentPage, int pageSize, T data){
        PageableBaseResponse baseResponse = new PageableBaseResponse();
        baseResponse.setData(data);
        baseResponse.setCode(HttpStatus.OK.value());
        baseResponse.setTotalHits(totalHits);
        baseResponse.setPageNumber(currentPage);
        baseResponse.setPageSize(pageSize);
        baseResponse.setTotalPages((int) Math.ceil((double) totalHits /pageSize));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}

