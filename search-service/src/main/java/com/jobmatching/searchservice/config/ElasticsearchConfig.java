// src/main/java/com/jobmatching/searchservice/config/ElasticsearchConfig.java
package com.jobmatching.searchservice.config;

import com.jobmatching.searchservice.model.JobDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

import jakarta.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.es.init-index", havingValue = "true", matchIfMissing = true)
public class ElasticsearchConfig {

    private final ElasticsearchOperations ops;

    @PostConstruct
    public void initIndex() {
        try {
            IndexOperations indexOps = ops.indexOps(JobDocument.class);
            Boolean exists = indexOps.exists();
            if (Boolean.FALSE.equals(exists)) {
                log.info("Elasticsearch: creating index '{}'", indexOps.getIndexCoordinates().getIndexName());
                indexOps.create();
                indexOps.putMapping(indexOps.createMapping());
                log.info("Elasticsearch: index created and mapping applied.");
            } else {
                log.info("Elasticsearch: index '{}' already exists.", indexOps.getIndexCoordinates().getIndexName());
            }
        } catch (Exception e) {
            // Đừng làm app chết — chỉ cảnh báo để bạn biết cần bật ES
            log.warn("Elasticsearch not reachable at startup. Skip index init. Cause: {}", e.getMessage());
        }
    }
}
