package com.jobmatching.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.HttpHeaders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoutingTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void userRoute_shouldForwardToUserService() {
        webTestClient.post()
                .uri("/api/users/login")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue("{\"email\": \"abc@test.com\", \"password\": \"123456\"}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void jobRoute_shouldForwardToJobService() {
        String fakeToken = "Bearer fake-valid-token";

        webTestClient.get()
                .uri("/api/jobs")
                .header(HttpHeaders.AUTHORIZATION, fakeToken)
                .exchange()
                .expectStatus().isOk();
    }
}
