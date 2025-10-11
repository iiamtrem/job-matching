package com.jobmatching.searchservice.web;

import com.jobmatching.searchservice.dto.*;
import com.jobmatching.searchservice.service.JobSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class JobSearchController {
    private final JobSearchService svc;

    // public search
    @GetMapping("/jobs")
    public PageResponse<JobDoc> search(@Valid JobSearchRequest req) {
        return svc.search(req);
    }

    // sync from job-service (requires X-API-Key if configured)
    @PostMapping("/jobs")
    public ResponseEntity<Void> upsert(@RequestBody @Valid JobDoc doc) {
        svc.upsert(doc);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
