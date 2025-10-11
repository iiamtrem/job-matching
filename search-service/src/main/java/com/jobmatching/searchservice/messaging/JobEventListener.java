package com.jobmatching.searchservice.messaging;

import com.jobmatching.searchservice.document.JobDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobEventListener {

    private final ElasticsearchOperations operations;

    @KafkaListener(topics = "job-events", groupId = "search-service")
    public void handle(JobEvent event) {
        switch (event.getType()) {
            case CREATED, UPDATED -> {
                JobDocument doc = JobDocument.builder()
                        .id(event.getJobId())
                        .title(event.getTitle())
                        .description(event.getDescription())
                        .company(event.getCompany())
                        .location(event.getLocation())
                        .build();
                operations.save(doc);
            }
            case DELETED -> operations.delete(event.getJobId(), JobDocument.class);
        }
    }
}
