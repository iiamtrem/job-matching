package com.jobmatching.jobservice.controller;

import com.jobmatching.jobservice.dto.JobCreationDto;
import com.jobmatching.jobservice.dto.JobUpdateDto;
import com.jobmatching.jobservice.model.Job;
import com.jobmatching.jobservice.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    public JobController(JobService jobService) { this.jobService = jobService; }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobCreationDto jobDto,
                                         @RequestHeader("X-User-Id") Long employerId) {
        return ResponseEntity.ok(jobService.createJob(jobDto, employerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }

    // /api/jobs?page=0&size=20&employerId=123 (employerId optional)
    @GetMapping
    public ResponseEntity<Page<Job>> listJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long employerId) {
        return ResponseEntity.ok(jobService.listJobs(employerId, PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id,
                                         @RequestBody JobUpdateDto dto,
                                         @RequestHeader("X-User-Id") Long employerId) {
        return ResponseEntity.ok(jobService.updateJob(id, dto, employerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id,
                                          @RequestHeader("X-User-Id") Long employerId) {
        jobService.deleteJob(id, employerId);
        return ResponseEntity.noContent().build();
    }
}
