package com.jobmatching.jobservice.integration;

import com.jobmatching.jobservice.dto.JobCreationDto;
import com.jobmatching.jobservice.model.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobIntegrationTest {

    @Autowired TestRestTemplate restTemplate;

    @Test
    void createJob_withValidToken_shouldReturnJob() {
        // Giả lập JWT hợp lệ (nếu chưa có thì hardcode)
        String token = "Bearer <your-valid-jwt>";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JobCreationDto dto = new JobCreationDto();
        dto.setTitle("Test Job");

        HttpEntity<JobCreationDto> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<Job> response = restTemplate.postForEntity("/api/jobs", entity, Job.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Job", response.getBody().getTitle());
    }
}
