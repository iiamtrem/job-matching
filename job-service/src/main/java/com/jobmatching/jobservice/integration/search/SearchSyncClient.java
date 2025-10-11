package com.jobmatching.jobservice.integration.search;

import com.jobmatching.jobservice.integration.search.dto.SearchJobDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchSyncClient {

    private final WebClient searchWebClient;

    @Value("${search.sync.enabled:true}")
    private boolean enabled;

    @Value("${search.sync.api-key:}")
    private String apiKey;

    public void upsert(SearchJobDoc doc) {
        if (!enabled) { log.debug("[search-sync] disabled, skip upsert {}", doc.getId()); return; }
        searchWebClient.post()
                .uri("/search/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-Key", apiKey)
                .bodyValue(doc)
                .retrieve()
                .toBodilessEntity()
                .doOnError(ex -> log.warn("[search-sync] upsert {} failed: {}", doc.getId(), ex.toString()))
                .onErrorResume(ex -> Mono.empty())
                .subscribe();
    }

    public void delete(long id) {
        if (!enabled) { log.debug("[search-sync] disabled, skip delete {}", id); return; }
        searchWebClient.delete()
                .uri("/search/jobs/{id}", id)
                .header("X-API-Key", apiKey)
                .retrieve()
                .toBodilessEntity()
                .doOnError(ex -> log.warn("[search-sync] delete {} failed: {}", id, ex.toString()))
                .onErrorResume(ex -> Mono.empty())
                .subscribe(); // fire-and-forget
    }
}
