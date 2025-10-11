package com.jobmatching.jobservice.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchSyncClient {

    private final RestTemplate restTemplate;

    @Value("${search.base-url}")
    private String baseUrl;

    public void upsert(JobPayload payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<JobPayload> req = new HttpEntity<>(payload, headers);
            restTemplate.exchange(baseUrl + "/internal/search/jobs", HttpMethod.POST, req, Void.class);
        } catch (Exception e) {
            log.warn("Failed to sync job to search-service id={}: {}", payload.getId(), e.getMessage());
        }
    }

    public void delete(String id) {
        try {
            restTemplate.exchange(baseUrl + "/internal/search/jobs/{id}", HttpMethod.DELETE, null, Void.class, id);
        } catch (Exception e) {
            log.warn("Failed to delete job in search-service id={}: {}", id, e.getMessage());
        }
    }
}
