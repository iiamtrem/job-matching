package com.jobmatching.jobservice.service;

import com.jobmatching.jobservice.model.Job;
import com.jobmatching.jobservice.repository.JobRepository;
import com.jobmatching.jobservice.search.JobPayload;
import com.jobmatching.jobservice.search.SearchSyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository repo;
    private final SearchSyncClient searchSync;

    public Job createJob(Job req) {
        Job saved = repo.save(req);
        searchSync.upsert(toPayload(saved));
        return saved;
    }

    public Job updateJob(Long id, Job req) {
        Job j = repo.findById(id).orElseThrow();
        // set lại các field bạn có trong entity:
        j.setTitle(req.getTitle());
        j.setDescription(req.getDescription());
        j.setRequirements(req.getRequirements());
        j.setSalaryMin(req.getSalaryMin());
        j.setSalaryMax(req.getSalaryMax());
        j.setLocation(req.getLocation());
        j.setJobType(req.getJobType());   // nếu là enum
        j.setStatus(req.getStatus());     // nếu có
        j.setEmployerId(req.getEmployerId()); // nếu có chỉnh sửa

        Job saved = repo.save(j);
        searchSync.upsert(toPayload(saved));
        return saved;
    }

    public void deleteJob(Long id) {
        repo.deleteById(id);
        searchSync.delete(String.valueOf(id));
    }

    private JobPayload toPayload(Job j) {
        return JobPayload.builder()
                .id(String.valueOf(j.getId()))
                .employerId(j.getEmployerId() == null ? null : String.valueOf(j.getEmployerId()))
                .title(j.getTitle())
                .description(j.getDescription())
                .requirements(j.getRequirements())
                .salaryMin(j.getSalaryMin())
                .salaryMax(j.getSalaryMax())
                .location(j.getLocation())
                .jobType(j.getJobType() == null ? null : j.getJobType().toString()) // enum -> String
                .status(j.getStatus() == null ? null : j.getStatus().toString())    // nếu enum
                .build();
    }
}
