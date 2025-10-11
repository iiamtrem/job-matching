package com.jobmatching.jobservice.config;

import java.time.Duration;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class SearchSyncConfig {

    @Bean
    public WebClient searchWebClient(
            WebClient.Builder builder,
            @Value("${search.sync.base-url}") String baseUrl,
            @Value("${search.sync.api-key}") String apiKey,
            @Value("${search.sync.timeout:2s}") Duration timeout
    ) {
        HttpClient http = HttpClient.create()
                .responseTimeout(timeout)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) timeout.toMillis());

        return builder
                .baseUrl(baseUrl)
                .defaultHeader("X-API-Key", apiKey)
                .clientConnector(new ReactorClientHttpConnector(http))
                .build();
    }
}
