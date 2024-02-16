package com.meesho.notification.configs;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
@Configuration
public class ElasticSearchConfigs extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Override
    @NonNull
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(clusterNodes)
                .build();
    }
}

