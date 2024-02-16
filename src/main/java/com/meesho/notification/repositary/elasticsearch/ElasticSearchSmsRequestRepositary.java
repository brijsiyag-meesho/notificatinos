package com.meesho.notification.repositary.elasticsearch;

import com.meesho.notification.models.entities.elasticsearch.ElasticSearchSmsRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchSmsRequestRepositary extends ElasticsearchRepository<ElasticSearchSmsRequest, Long> {

}
