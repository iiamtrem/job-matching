package com.jobmatching.searchservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobmatching.searchservice.dto.JobUpsertRequest;
import com.jobmatching.searchservice.document.JobDocument;
import com.jobmatching.searchservice.service.JobIndexService;

@RestController
@RequestMapping("/internal/search/jobs")
@RequiredArgsConstructor
public class SearchInternalController {

    private final JobIndexService jobIndexService;

    @PostMapping
    public ResponseEntity<Void> upsert(@RequestBody JobUpsertRequest req) {
        JobDocument doc = JobDocument.builder()
                .id(req.getId())
                .employerId(req.getEmployerId())
                .title(req.getTitle())
                .description(req.getDescription())
                .requirements(req.getRequirements())
                .salaryMin(req.getSalaryMin())
                .salaryMax(req.getSalaryMax())
                .location(req.getLocation())
                .jobType(req.getJobType())
                .status(req.getStatus())
                .build();
        jobIndexService.upsert(doc);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        jobIndexService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
