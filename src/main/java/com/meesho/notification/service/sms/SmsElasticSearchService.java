package com.meesho.notification.service.sms;

import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.models.entities.elasticsearch.ElasticSearchSmsRequest;
import com.meesho.notification.repositary.elasticsearch.ElasticSearchSmsRequestRepositary;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsElasticSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticSearchSmsRequestRepositary elasticSearchSmsRequestRepositary;

    @Autowired
    public SmsElasticSearchService(ElasticsearchOperations elasticsearchOperations, ElasticSearchSmsRequestRepositary elasticSearchSmsRequestRepositary) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticSearchSmsRequestRepositary = elasticSearchSmsRequestRepositary;
    }


    public void saveSmsRequest(SmsRequest smsRequest){
        elasticSearchSmsRequestRepositary.save(smsRequest.toElasticsearchSmsRequest());
    }

    public SearchHits<ElasticSearchSmsRequest> getAllSmsRequests(LocalDateTime from, LocalDateTime to, String searchString, Pageable pageable) {
        CriteriaQuery query = buildSearchQuery(from, to, searchString);
        query.setPageable(pageable);
        return elasticsearchOperations.search(query, ElasticSearchSmsRequest.class);
    }

    private CriteriaQuery buildSearchQuery(LocalDateTime from, LocalDateTime to, String searchString){
        Criteria criteria = new Criteria();
        if (from != null && to != null){
            if(!from.isBefore(to)){
                throw new IllegalArgumentException("from Date can't be after to Date.");
            }
            criteria.and(new Criteria("createdAt").greaterThanEqual(from).lessThanEqual(to));
        }
        if(StringUtils.isNotBlank(searchString)){
            String[] words = searchString.split("\\s+");
            for (String word : words) {
                criteria.and(new Criteria("message").contains(word));
            }
        }
        return new CriteriaQuery(criteria);
    }
}
