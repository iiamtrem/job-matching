package com.jobmatching.jobservice.controller;

import com.jobmatching.jobservice.dto.JobCreationDto;
import com.jobmatching.jobservice.dto.JobUpdateDto;
import com.jobmatching.jobservice.model.Job;
import com.jobmatching.jobservice.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/{id}")
    public ResponseEntity<Job> get(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }

    @GetMapping
    public ResponseEntity<Page<Job>> list(@RequestParam(defaultValue="0") int page,
                                          @RequestParam(defaultValue="20") int size,
                                          @RequestParam(required=false) Long employerId) {
        return ResponseEntity.ok(jobService.listJobs(employerId, PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<Job> create(@RequestBody @Valid JobCreationDto dto, Authentication auth) {
        Long employerId = Long.valueOf((String) auth.getPrincipal());
        return ResponseEntity.ok(jobService.createJob(dto, employerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> update(@PathVariable Long id,
                                      @RequestBody JobUpdateDto dto,
                                      Authentication auth) {
        Long employerId = Long.valueOf((String) auth.getPrincipal());
        return ResponseEntity.ok(jobService.updateJob(id, dto, employerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        Long employerId = Long.valueOf((String) auth.getPrincipal());
        jobService.deleteJob(id, employerId);
        return ResponseEntity.noContent().build();
    }
}
