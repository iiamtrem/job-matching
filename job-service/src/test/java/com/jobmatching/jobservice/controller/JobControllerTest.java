package com.jobmatching.jobservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatching.jobservice.dto.JobCreationDto;
import com.jobmatching.jobservice.model.Job;
import com.jobmatching.jobservice.service.JobService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean JobService jobService;

    @Test
    @WithMockUser(roles = "EMPLOYER")
    void createJob_success() throws Exception {
        JobCreationDto dto = new JobCreationDto();
        dto.setTitle("Java Developer");

        Job job = new Job();
        job.setId(1L);
        job.setTitle("Java Developer");

        Mockito.when(jobService.createJob(any(), anyLong())).thenReturn(job);

        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Developer"));
    }
}
